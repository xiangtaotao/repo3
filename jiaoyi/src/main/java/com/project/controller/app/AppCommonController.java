package com.project.controller.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.project.dao.CategoryRepository;
import com.project.dao.CollectionRepository;
import com.project.dao.CommentRepository;
import com.project.dao.GoodsRepository;
import com.project.dao.MessageRepository;
import com.project.dao.OrderRepository;
import com.project.dao.UserRepository;
import com.project.enums.ResultEnum;
import com.project.pojo.CateGory;
import com.project.pojo.Collection;
import com.project.pojo.Comment;
import com.project.pojo.Goods;
import com.project.pojo.GoodsOrder;
import com.project.pojo.GoodsOrder.OrderDetail;
import com.project.pojo.Message;
import com.project.pojo.User;
import com.project.utils.ResultVOUtil;
import com.project.utils.SortTools;
import com.project.vo.PageQuery;
import com.project.vo.PageResultVO;
import com.project.vo.ResultVO;

@RestController
@RequestMapping("/common")
public class AppCommonController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private GoodsRepository goodsRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private CollectionRepository collectRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String ORDER_DETAIL = "buycart";

	/*
	 * 获取所有分类
	 */
	@PostMapping("/categoryList")
	public ResultVO<?> list() {
		List<CateGory> findAll = categoryRepository.findAll();
		return ResultVOUtil.success(findAll);
	}

	/*
	 * 根据分类查询所有商品
	 */
	@PostMapping("/findByCategory")
	public ResultVO<?> findByCategory(Long categoryId) {
		if (StringUtils.isEmpty(categoryId)) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		return ResultVOUtil.success(goodsRepository.findAllByCategoryId(categoryId));
	}

	/*
	 * 相关推荐
	 */
	@PostMapping("/findGoods")
	public ResultVO<?> findGoods(@Valid PageQuery pageQuery) {
		Page<Goods> page = goodsRepository.findAll(pageQuery.getPageable());
		return ResultVOUtil.success(PageResultVO.of(page.getContent(), page));
	}

	/*
	 * 根据商品名称搜索
	 */
	@PostMapping("/findGoodsByTitle")
	public ResultVO<?> findGoodsByTitle(String title) {
		if (StringUtils.isEmpty(title)) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		return ResultVOUtil.success(goodsRepository.findAllByTitle(title));
	}

	/*
	 * 发布商品
	 */
	@PostMapping("/pushGoods")
	public ResultVO<?> pushGoods(Goods goods, String token) {
		if (StringUtils.isEmpty(goods.getCategoryId()) || StringUtils.isEmpty(goods.getDetail())
				|| StringUtils.isEmpty(goods.getGoodsImage()) || StringUtils.isEmpty(goods.getDispacherPrice())
				|| StringUtils.isEmpty(goods.getGoodsAddress()) || StringUtils.isEmpty(goods.getGoodsPrice())
				|| StringUtils.isEmpty(goods.getTitle()) || StringUtils.isEmpty(token)) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		String user = stringRedisTemplate.opsForValue().get(token);
		User parse = (User) JSONObject.parseObject(user, User.class);
		goods.setUserId(parse.getId());
		goods.setAddTime(System.currentTimeMillis());
		goods.setCount(100);
		Message message = new Message();
		message.setAddTime(System.currentTimeMillis());
		message.setIsRead(1);
		message.setMessaage("用户" + parse.getPhone() + "发布了商品" + goods.getTitle());
		message.setUserId(-1L);
		messageRepository.save(message);
		return ResultVOUtil.success(goodsRepository.save(goods));
	}

	/*
	 * 获取系统消息
	 */
	@PostMapping("/findMessage")
	public ResultVO<?> findMessage() {
		return ResultVOUtil.success(messageRepository.findAll(SortTools.basicSort()));
	}

	/*
	 * 个人信息
	 */
	@PostMapping("/userInfor")
	public ResultVO<?> userInfor(String token) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		return ResultVOUtil.success(userRepository.findById(userInfor.getId()));
	}

	/*
	 * 我发布的商品
	 */
	@PostMapping("/userGoods")
	public ResultVO<?> userGoods(String token) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		return ResultVOUtil.success(goodsRepository.findAllByUserId(userInfor.getId()));
	}

	/*
	 * 购物车下单
	 */
	@PostMapping("/createOrderFromCart")
	public ResultVO<?> createOrderByCart(String token) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		String cartFlag = ORDER_DETAIL.concat(":").concat(userInfor.getId().toString());
		GoodsOrder goods = new GoodsOrder();
		List<OrderDetail> parseArray = new ArrayList<>();
		if (stringRedisTemplate.hasKey(cartFlag)) {
			String good = stringRedisTemplate.opsForValue().get(cartFlag);
			parseArray = JSONObject.parseArray(good, OrderDetail.class);
			BigDecimal total = parseArray.stream().map(s -> s.getUnitPrice().multiply(new BigDecimal(s.getCount())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			goods.setUserId(userInfor.getId());
			goods.setGoodsUserId(parseArray.get(0).getGoodsId());
			goods.setAddTime(System.currentTimeMillis());
			goods.setPayStatus(2);
			goods.setAmount(total);
			goods.setAddTime(System.currentTimeMillis());
			goods.setDetail(JSONObject.toJSONString(parseArray));
			goods.setGoodsName(parseArray.get(0).getGoodsName());
			goods.setGoodsId(parseArray.get(0).getGoodsId());
		}
		GoodsOrder save = orderRepository.save(goods);
		stringRedisTemplate.delete(cartFlag);
		// 清空购物车
		return ResultVOUtil.success(save);
	}

	/*
	 * 直接下单
	 */
	@PostMapping("/createOrder")
	public ResultVO<?> createOrder(String token, OrderDetail orderDetail) {
		if (StringUtils.isEmpty(orderDetail.getGoodsId()) || StringUtils.isEmpty(orderDetail.getCount())
				|| StringUtils.isEmpty(orderDetail.getUnitPrice()) || StringUtils.isEmpty(orderDetail.getGoodsName())) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		if (orderDetail.getCount() <= 0) {
			return ResultVOUtil.error("购买数量必须大于0");
		}
		Goods goods1 = goodsRepository.findById(orderDetail.getGoodsId()).orElse(new Goods());
		GoodsOrder goods = new GoodsOrder();
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		goods.setUserId(userInfor.getId());
		goods.setGoodsUserId(goods1.getUserId());
		goods.setAddTime(System.currentTimeMillis());
		goods.setPayStatus(2);
		goods.setAmount(new BigDecimal(orderDetail.getCount()).multiply(goods1.getGoodsPrice()).setScale(2, 2));
		goods.setDetail(JSONObject.toJSONString(orderDetail));
		goods.setAddTime(System.currentTimeMillis());
		goods.setGoodsName(goods1.getTitle());
		goods.setGoodsId(orderDetail.getGoodsId());
		GoodsOrder save = orderRepository.save(goods);
		return ResultVOUtil.success(save);
	}

	/*
	 * 添加修改删除购物车
	 */
	@PostMapping("/createBuyCart")
	public ResultVO<?> createBuyCart(String token, OrderDetail orderDetail) {
		if (StringUtils.isEmpty(orderDetail.getGoodsId()) || StringUtils.isEmpty(orderDetail.getCount())
				|| StringUtils.isEmpty(orderDetail.getUnitPrice()) || StringUtils.isEmpty(orderDetail.getGoodsName())) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		String cartFlag = ORDER_DETAIL.concat(":").concat(userInfor.getId().toString());
		List<OrderDetail> parseArray = new ArrayList<>();
		boolean flag = false;
		if (stringRedisTemplate.hasKey(cartFlag)) {
			String good = stringRedisTemplate.opsForValue().get(cartFlag);
			parseArray = JSONObject.parseArray(good, OrderDetail.class);
			for (OrderDetail s : parseArray) {
				if (s.getGoodsId().equals(orderDetail.getGoodsId())) {
					flag = true;
					break;
				}
			}
			Iterator<OrderDetail> iterator = parseArray.iterator();
			while (iterator.hasNext()) {
				OrderDetail s = iterator.next();
				if (s.getGoodsId().equals(orderDetail.getGoodsId()) && orderDetail.getCount() > 0) {
					s.setCount(orderDetail.getCount());
				} else if (s.getGoodsId().equals(orderDetail.getGoodsId()) && orderDetail.getCount() <= 0) {
					iterator.remove();
				}
			}
			if (!flag) {
				parseArray.add(orderDetail);
			}
			stringRedisTemplate.delete(cartFlag);
		} else {
			parseArray.add(orderDetail);
		}
		// 添加购物车
		stringRedisTemplate.opsForValue().set(cartFlag, JSONObject.toJSONString(parseArray), 1, TimeUnit.HOURS);
		return ResultVOUtil.success();
	}

	/*
	 * 获取购物车信息
	 */
	@PostMapping("/getCartInfor")
	public ResultVO<?> getOrderDetailByCart(String token) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		String cartFlag = ORDER_DETAIL.concat(":").concat(userInfor.getId().toString());
		List<OrderDetail> parseArray = null;
		if (stringRedisTemplate.hasKey(cartFlag)) {
			String good = stringRedisTemplate.opsForValue().get(cartFlag);
			parseArray = JSONObject.parseArray(good, OrderDetail.class);
		}
		// 添加购物车
		return ResultVOUtil.success(parseArray);
	}

	/*
	 * 虚拟支付
	 */
	@PostMapping("/pay")
	public ResultVO<?> createOrder(Long orderId) {
		if (StringUtils.isEmpty(orderId)) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		GoodsOrder goodsOrder = orderRepository.findById(orderId).orElse(null);
		if (goodsOrder == null) {
			return ResultVOUtil.error("订单不存在");
		}
		goodsOrder.setPayStatus(1);
		return ResultVOUtil.success(orderRepository.save(goodsOrder));
	}

	/*
	 * 我卖出的订单
	 */
	@PostMapping("/sellOrder")
	public ResultVO<?> sellOrder(String token) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		String sql = " SELECT a.`pay_status` payStatus,a.`amount` ,a.`goods_name` title,a.`add_time` `addTime`,b.`goods_image` goodsImage,"
				+ " b.`dispacher_price` dispacherPrice,b.`detail`,b.id goodsId,d.phone,a.id orderId FROM goods_order a "
				+ " inner join goods b on a.`goods_id`=b.`id`" + " INNER JOIN USER d ON a.`goods_user_id`=d.id"
				+ " WHERE a.`goods_user_id`=? and a.pay_status!=3";
		return ResultVOUtil.success(jdbcTemplate.queryForList(sql, userInfor.getId()));
	}

	/*
	 * 我买入的订单
	 */
	@PostMapping("/buyOrder")
	public ResultVO<?> buyOrder(String token) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		String sql = " SELECT a.`pay_status` payStatus,a.`amount` ,a.`goods_name` title,a.`add_time` `addTime`,b.`goods_image` goodsImage,"
				+ " b.`dispacher_price` dispacherPrice,b.`detail`,b.id goodsId,d.phone,a.id orderId FROM goods_order a "
				+ " inner join goods b on a.`goods_id`=b.`id`" + " INNER JOIN USER d ON a.`user_id`=d.id"
				+ " WHERE a.`user_id`=? and a.pay_status!=3";
		return ResultVOUtil.success(jdbcTemplate.queryForList(sql, userInfor.getId()));
	}

	/*
	 * 取消订单
	 */
	@PostMapping("/cancelOrder")
	public ResultVO<?> cancelOrder(Long orderId, String token) {
		if (StringUtils.isEmpty(orderId)) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		String sql = " update goods_order set pay_status=3 where id=?";
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		// 删除购物车
		String cartFlag = ORDER_DETAIL.concat(":").concat(userInfor.getId().toString());
		if (stringRedisTemplate.hasKey(cartFlag)) {
			stringRedisTemplate.delete(cartFlag);
		}
		return ResultVOUtil.success(jdbcTemplate.update(sql, orderId));
	}

	/*
	 * 收藏商品
	 */
	@PostMapping("/collectionGoods")
	public ResultVO<?> collectionGoods(String token, Collection collection) {
		if (StringUtils.isEmpty(collection.getGoodsId())) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		if (StringUtils.isEmpty(collection.getId())) {
			collection.setAddTime(System.currentTimeMillis());
			collection.setUserId(userInfor.getId());
			collection.setGoodsId(collection.getGoodsId());
			return ResultVOUtil.success(collectRepository.save(collection).getId());
		} else {
			collectRepository.deleteById(collection.getId());
			return ResultVOUtil.success("已取消收藏");
		}
	}

	/*
	 * 我收藏的商品
	 */
	@PostMapping("/collectionGoodsList")
	public ResultVO<?> collectionGoodsList(String token) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		String sql = " SELECT c.`add_time` `addTime`,c.`category_id` categoryId,c.`detail`,c.`dispacher_price` dispacherPrice ,"
				+ " c.`goods_address` goodsAddress,c.`goods_image` goodsImage,c.`goods_price` goodsPrice,c.`title`,c.`id`,c.`user_id` userID,"
				+ " a.id AS collectId FROM collection" + " a  INNER JOIN USER b ON a.`user_id`=b.`id`"
				+ " INNER JOIN goods c ON c.`id`=a.`goods_id` " + " WHERE a.`user_id`=?";
		return ResultVOUtil.success(jdbcTemplate.queryForList(sql, userInfor.getId()));
	}

	/*
	 * 评价商品
	 */
	@PostMapping("/commonGood")
	public ResultVO<?> commonGoods(String content, Long goodId, String token) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		if (content == null || goodId == null) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		Comment c = new Comment();
		c.setUserId(userInfor.getId());
		c.setContent(content);
		c.setGoodId(goodId);
		c.setAddTime(System.currentTimeMillis());
		commentRepository.save(c);
		return ResultVOUtil.success();
	}

	/*
	 * 商品详情
	 */
	@PostMapping("/goodDetail")
	public ResultVO<?> goodDetail(Long id, String token) {
		if (id == null) {
			return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
		}
		Goods goods = goodsRepository.findById(id).orElse(new Goods());
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		goods.setUserPhone(userInfor.getPhone());
		goods.setCList(jdbcTemplate.queryForList(
				"SELECT a.`content`,b.`headimg`,b.`username`,b.`nickname`,b.`phone` FROM COMMENT a"
						+ " inner join user b on a.`user_id`=b.`id`" + " WHERE a.`good_id`=? order by a.id desc",
				goods.getId()));
		return ResultVOUtil.success(goods);
	}

	/*
	 * 修改个人信息
	 */
	@PostMapping("/setUser")
	public ResultVO<?> setUser(String token, User users) {
		String user = stringRedisTemplate.opsForValue().get(token);
		User userInfor = (User) JSONObject.parseObject(user, User.class);
		if (!StringUtils.isEmpty(users.getAddress())) {
			userInfor.setAddress(users.getAddress());
		}
		if (!StringUtils.isEmpty(users.getHeadImg())) {
			userInfor.setHeadImg(users.getHeadImg());
		}
		if (!StringUtils.isEmpty(users.getNickName())) {
			userInfor.setNickName(users.getNickName());
		}
		if (!StringUtils.isEmpty(users.getPassword())) {
			userInfor.setPassword(users.getPassword());
		}
		if (!StringUtils.isEmpty(users.getPhone())) {
			userInfor.setPhone(users.getPhone());
		}
		if (!StringUtils.isEmpty(users.getSchool())) {
			userInfor.setSchool(users.getSchool());
		}
		if (!StringUtils.isEmpty(users.getUserName())) {
			userInfor.setUserName(users.getUserName());
		}
		return ResultVOUtil.success(userRepository.save(userInfor));
	}

}
