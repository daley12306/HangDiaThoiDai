package vn.hangdiathoidai.services;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPAYService {

	int orderReturn(HttpServletRequest request);

	String createOrder(HttpServletRequest request, int amount, String orderInfor, String urlReturn);
	
}
