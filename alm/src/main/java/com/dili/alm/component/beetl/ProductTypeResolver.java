package com.dili.alm.component.beetl;

import com.dili.alm.domain.Product;
import org.beetl.core.Context;
import org.beetl.core.VirtualAttributeEval;
import org.springframework.stereotype.Component;

/**
 * Created by asiamaster on 2017/6/20 0020.
 */
@Component
public class ProductTypeResolver implements VirtualAttributeEval {
	@Override
	public boolean isSupport(Class c, String attributeName) {
		return c.equals(Product.class);
	}

	@Override
	public Object eval(Object o, String attributeName, Context ctx) {
		Product user = (Product) o;
		if(attributeName.equals("type") && user.getType() != null){
			if(user.getType().equals(10)){
				return "蔬菜";
			}else if(user.getType().equals(20)){
				return "水果";
			}else if(user.getType().equals(20)){
				return "日杂";
			}
		}
		return "";
	}
}
