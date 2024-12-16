package vn.hangdiathoidai.services;

import java.util.List;

import vn.hangdiathoidai.entity.ProductsSku;

public interface ProductSkuService {

	void updateSku(Long skuId, ProductsSku updatedSku);

	ProductsSku getSkuById(Long skuId);

	List<ProductsSku> getSkuByProductId(Long productId);

    ProductsSku findByProductId(Long productId);
}
