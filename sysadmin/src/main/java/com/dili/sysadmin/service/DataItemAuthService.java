package com.dili.sysadmin.service;

import java.util.List;

import com.dili.sysadmin.domain.dto.DataAuthConfDto;

/**
 * Created by Administrator on 2015/10/4.
 */
public interface DataItemAuthService extends DataItemFetcher{


	public List<DataAuthConfDto> fetchItemList();
}
