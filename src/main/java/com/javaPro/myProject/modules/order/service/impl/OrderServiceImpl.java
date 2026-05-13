package com.javaPro.myProject.modules.order.service.impl;

import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.modules.message.dao.MessageDao;
import com.javaPro.myProject.modules.message.entity.Message;
import com.javaPro.myProject.modules.order.dao.OrderDao;
import com.javaPro.myProject.modules.order.entity.Order;
import com.javaPro.myProject.modules.order.service.OrderService;
import com.javaPro.myProject.modules.product.dao.ProductDao;
import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.shopcart.dao.ShopcartDao;
import com.javaPro.myProject.modules.shopcart.entity.Shopcart;
import com.javaPro.myProject.modules.sysuser.dao.SysuserDao;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单表(Order)表服务实现类
 *
 * @author
 * @since 21:57:54
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
@Resource
private ShopcartDao shopcartDao;
    @Resource
    private ProductDao productDao;
@Resource
private SysuserDao sysuserDao;
@Resource
private MessageDao messageDao;
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Order queryById(Integer id) {
        return this.orderDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param order 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Order> queryByPage(Order order) {
        List<Order> orders = this.orderDao.queryAllByLimit(order);
        if (!CollectionUtils.isEmpty(orders)){
            for (Order order1 : orders) {
                String carid = order1.getCarid();
                String[] split = carid.split(",");
                List<String> list = Arrays.asList(split);
                List<Shopcart> list1 = new ArrayList<>();
                for (String s : list) {
                    Shopcart shopcart = shopcartDao.queryProductByCartId(s);
                    list1.add(shopcart);
                }
                order1.setCartList(list1);
            }
        }

        return orders;
    }

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    @Override
    public AjaxResult insert(Order order) {
        List<Shopcart> shopcarts;

        // 如果有选中的购物车ID，只处理选中的商品
        if (order.getSelectedCartIds() != null && !order.getSelectedCartIds().isEmpty()) {
            String[] cartIds = order.getSelectedCartIds().split(",");
            shopcarts = new ArrayList<>();
            for (String cartId : cartIds) {
                Shopcart cart = shopcartDao.queryById(Integer.parseInt(cartId.trim()));
                if (cart != null && cart.getSpare1().equals("0")) { // 确保是未下单的商品
                    shopcarts.add(cart);
                }
            }
        } else {
            // 如果没有指定选中商品，则处理所有未下单商品（保持原有逻辑）
            Shopcart shopcart = new Shopcart();
            shopcart.setUserid(order.getUserid());
            shopcart.setSpare1("0");
            shopcarts = shopcartDao.queryAllByLimit(shopcart);
        }
        Sysuser sysuser = sysuserDao.queryById(order.getUserid());
        if (sysuser == null) {
            return AjaxResult.error("用户不存在");
        }
        Double money = sysuser.getMoney() == null ? 0.0 : sysuser.getMoney();
        Double sum = 0.0;
        for (Shopcart item : shopcarts) {
            Product product = productDao.queryById(item.getProductid());
            String kedanjia = product.getKedanjia();
            double itemPrice = Double.parseDouble(kedanjia);

            // 计算折扣价格
            if (product.getSpare2() != null && !product.getSpare2().isEmpty()) {
                try {
                    double discount = Double.parseDouble(product.getSpare2());
                    if (discount > 0 && discount < 10) {
                        itemPrice = itemPrice * discount / 10;
                    }
                } catch (NumberFormatException e) {
                    // 如果折扣格式不正确，使用原价
                }
            }

            // 乘以购买数量
            sum = sum + (itemPrice * item.getNumber());
        }
        if (money < sum){
            return AjaxResult.error("余额不足！");
        }else {
            money = money - Double.parseDouble(String.valueOf(sum));
            sysuser.setMoney( money);
            sysuserDao.update(sysuser);

            List<String> collect = shopcarts.stream().map(t -> t.getId() + "").collect(Collectors.toList());
//            String s = String.join(",",collect);
//            order.setCarid(s);
            // 记录需要通知的服务商ID，避免重复通知
            Set<Integer> notifiedCompanies = new HashSet<>();

            for (Shopcart shopcart1 : shopcarts) {
                Order order1 = new Order();
                order1.setUserid(order.getUserid());
                order1.setUsername(order.getUsername());
                order1.setRemark(order.getRemark());
                order1.setCarid(shopcart1.getId() + "");
                this.orderDao.insert(order1);

                // 获取商品信息以确定服务商
                Product product = productDao.queryById(shopcart1.getProductid());
                if (product != null && product.getCompanyid() != null) {
                    Integer companyId = product.getCompanyid();

                    // 销量现在通过历史订单动态计算，不需要手动更新spare1字段

                    // 如果该服务商还没有被通知过，则发送通知
                    if (!notifiedCompanies.contains(companyId)) {
                        // 发送订单通知给对应的服务商
                        Message message = new Message();
                        message.setSenderid(order.getUserid()); // 发送人是下单用户
                        message.setReceiveid(companyId); // 接收人是服务商
                        message.setContent("您有新的订单，订单用户：" + order.getUsername() + "，联系方式：" + sysuser.getPhonenumber());
                        message.setStatus("0"); // 未处理状态
                        message.setCreatetime(new Date());
                        messageDao.insert(message);

                        notifiedCompanies.add(companyId);
                    }
                }
            }

            for (String s1 : collect) {
                Shopcart shopcart1 = new Shopcart();
                shopcart1.setId(Integer.parseInt(s1));
                shopcart1.setSpare1("1");
                shopcartDao.update(shopcart1);
            }
        }



        return AjaxResult.ok();
    }

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    @Override
    public Order update(Order order) {
        this.orderDao.update(order);
        return this.queryById(order.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.orderDao.deleteById(id) > 0;
    }

    @Override
    public AjaxResult statistics( ) {
        Map<String, Object> resMap = new HashMap<>();
        Product product = new Product();
        List<Map<String,Object>> maps = new ArrayList<>();
        List<Object> list = new ArrayList<>();//存放饼图的三类数据
        List<Object> list2 = new ArrayList<>();//存放折线图进十日数据
        List<Object> list3 = new ArrayList<>();//存放折线图进十日数据
        //查询资产类型及其使用次数
        List<Product> products = this.productDao.queryAllByLimit(product);

        //饼图
        Map<String, List<Product>> collect = products.stream().collect(Collectors.groupingBy(Product::getTypeName));
        Set<Map.Entry<String, List<Product>>> entries = collect.entrySet();
        for (Map.Entry<String, List<Product>> entry : entries) {
            String key = entry.getKey();
            Map<String,Object> map = new HashMap<>();
            map.put("name",key);
            List<Product> value = entry.getValue();
            if (CollectionUtils.isEmpty(value)){
                map.put("value",0);
            }else {
                map.put("value",value.size());
            }
            list.add(map);
        }


        resMap.put("topMap",list);



        //折线图
        Order tApply = new Order();


        List<Order> tApplies = orderDao.queryAllByLimit(tApply);
        Map<String, List<Order>> collect2 = tApplies.stream().collect(Collectors.groupingBy(t -> Calendar.getInstance().get(Calendar.YEAR) + "-" + (t.getCreatetime().getMonth() + 1) + "-" + t.getCreatetime().getDate()));
        for (Map.Entry<String, List<Order>> stringListEntry : collect2.entrySet()) {
            Map<String, Object> map3 = new HashMap<>();
            List<Order> value = stringListEntry.getValue();
            map3.put("period", stringListEntry.getKey());
            map3.put("licensed", org.apache.commons.collections4.CollectionUtils.isNotEmpty(value)?value.size():0);
            list2.add(map3);
        }
        resMap.put("bottomMap", list2);

//        柱状图
        Message tApply2 = new Message();


        List<Message> tApplies2 = messageDao.queryAllByLimit(tApply2);
        Map<String, List<Message>> collect3 = tApplies2.stream().collect(Collectors.groupingBy(t -> t.getStatus() ));
        for (Map.Entry<String, List<Message>> stringListEntry : collect3.entrySet()) {
            Map<String, Object> map3 = new HashMap<>();
            List<Message> value = stringListEntry.getValue();
            if (stringListEntry.getKey().equals("1")){
                map3.put("period", "已回复");
            }
            if (stringListEntry.getKey().equals("0")){
                map3.put("period", "未回复");
            }
            map3.put("a", value.stream().filter(t->t.getStatus().equals("1")).collect(Collectors.toList()).size());
            map3.put("b", value.stream().filter(t->t.getStatus().equals("0")).collect(Collectors.toList()).size());
            list3.add(map3);
        }
        resMap.put("thirdMap", list3);
        return AjaxResult.ok(resMap) ;
    }
}
