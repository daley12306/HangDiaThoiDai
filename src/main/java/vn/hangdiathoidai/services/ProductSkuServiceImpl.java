package vn.hangdiathoidai.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.ProductsSku;
import vn.hangdiathoidai.repository.ProductSkuRepository;

@Service
public class ProductSkuServiceImpl implements ProductSkuService {
	@Autowired
    private ProductSkuRepository productsSkuRepository;
	
	@Override
	public ProductsSku getSkuById(Long skuId) {
        return productsSkuRepository.findById(skuId).orElse(null);
    }

    // Cập nhật SKU
    @Override
	public void updateSku(Long skuId, ProductsSku updatedSku) {
        ProductsSku existingSku = productsSkuRepository.findById(skuId).orElse(null);
        if (existingSku != null) {
            existingSku.setPrice(updatedSku.getPrice());
            existingSku.setQuantity(updatedSku.getQuantity());
            productsSkuRepository.save(existingSku);  // Lưu SKU đã chỉnh sửa
        }
    }
    
	@Override
	public List<ProductsSku> getSkuByProductId(Long productId) {
		return productsSkuRepository.findByProductId(productId);
	}
}

