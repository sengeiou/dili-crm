package com.dili.alm.provider;

import com.dili.alm.cache.AlmCache;
import com.dili.alm.domain.Project;
import com.dili.alm.service.ProjectService;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Component
public class ProjectProvider implements ValueProvider, ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	ProjectService projectService;


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}

	public void init(){
		if(AlmCache.projectMap.isEmpty()){
			List<Project> list = projectService.list(DTOUtils.newDTO(Project.class));
			list.forEach(project -> {
				AlmCache.projectMap.put(project.getId(), project);
			});
		}
	}

	@Override
	public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
		init();
		ArrayList buffer = new ArrayList<ValuePair<?>>();
		AlmCache.projectMap.forEach((k, v)->{
			buffer.add(new ValuePairImpl(v.getName(), k));
		});
		return buffer;
	}

	@Override
	public String getDisplayText(Object o, Map map, FieldMeta fieldMeta) {
		if(o == null) return null;
		init();
		Project project = AlmCache.projectMap.get(o);
		return project == null ? null : project.getName();
	}

}
