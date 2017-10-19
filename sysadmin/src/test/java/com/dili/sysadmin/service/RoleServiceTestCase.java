package com.dili.sysadmin.service;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dili.SysadminApplication;
import com.dili.sysadmin.domain.Role;
import com.dili.sysadmin.service.RoleService;

public class RoleServiceTestCase {

	@Test
	public void test() {
		Pattern p = Pattern
				.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$");
		System.out.println(p.matcher("asdasdfasf").matches());
	}

}
