package com.robin.bean;

public class CartItem {
	// 商品
	// 商品数目
	// 价格小结
	private Product product;
	private  int count;
	private double subtotal;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getSubtotal() {
		return product.getShop_price()*count;
	}
	@Override
	public String toString() {
		return "CartItem [product=" + product.getPid() + ", count=" + count + ", subtotal=" + subtotal + "]";
	}
}
