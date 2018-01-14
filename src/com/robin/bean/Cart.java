package com.robin.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {

	// 购物项集合 key为pid,value为cartItem
	// 总价格
	private Map<String, CartItem> cartItemMap = new HashMap<String, CartItem>();
	private double total;
	public Map<String, CartItem> getCartItemMap() {
		return cartItemMap;
	}
	public void setCartItemMap(Map<String, CartItem> cartItemMap) {
		this.cartItemMap = cartItemMap;
	}
	public double getTotal() {
		return total;
	}
	public Collection<CartItem> getCartItems() {
		Collection<CartItem> values = cartItemMap.values();
		if(values == null)
		{
			values = new ArrayList<>();
		}
		return values;
	}
	
	// 将商品添加进购物车
	public void addToCart(CartItem cartItem) {
		// 1.判断这件商品是否已经在Map里面了
		String pid = cartItem.getProduct().getPid();
		if(cartItemMap.containsKey(pid))
		{
			// 已经在map里面,只能修改Map里面对应商品的数目和小结
			CartItem old_cartItem = cartItemMap.get(pid);
			int new_count = old_cartItem.getCount()+cartItem.getCount();
			old_cartItem.setCount(new_count);
			
			// 修改总额
			total += cartItem.getSubtotal();
		}else
		{
			// 不在map里面,在Map里面新增这个购物项
			cartItemMap.put(pid, cartItem);
			total += cartItem.getSubtotal();
		}
	}
	
	// 移除出购物车
	public void removeFromCart(String pid) {
		// 从map中移除
		CartItem cartItem = cartItemMap.remove(pid);
		
		// 总金额减少
		if(cartItem != null)
		{
			total -= cartItem.getSubtotal();
		}
	}
	
	// 清空购物车
	public void clearCart() {
		cartItemMap.clear();
		total = 0.0;
	}
	
	
	
	
}
