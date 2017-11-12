/**
 * 
 */
package com.tvd12.ezyfoxserver;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.mapping.jackson.EzyJsonMapper;
import com.tvd12.ezyfoxserver.mapping.jackson.EzySimpleJsonMapper;
import com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySettingsReader;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettingsReader;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyRequestMappers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventPluginsMapperImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyManagersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyRequestMappersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyServerControllersImpl;

/**
 * @author tavandung12
 *
 */
public class EzyLoader extends EzyLoggable {
    
    protected EzyConfig config;
    protected ClassLoader classLoader;
    
    public EzyServer load() {
        EzySettings settings = readSettings();
    	EzySimpleServer answer = new EzySimpleServer();
    	answer.setConfig(config);
    	answer.setSettings(settings);
    	answer.setManagers(newManagers());
    	answer.setClassLoader(classLoader);
    	answer.setJsonMapper(newJsonMapper());
    	answer.setStatistics(newStatistics());
    	answer.setControllers(newControllers());
    	answer.setRequestMappers(newRequestMapper());
    	answer.setAppClassLoaders(newAppClassLoaders());
    	answer.setEventPluginsMapper(newEventPluginsMapper(settings));
    	return answer;
    }
    
    protected EzySettings readSettings() {
    	return newSettingsReader().read();
    }
    
    protected EzySettingsReader newSettingsReader() {
        return EzySimpleSettingsReader.builder()
                .homePath(getHomePath())
                .classLoader(classLoader)
                .build();
    }
    
    protected EzyJsonMapper newJsonMapper() {
        return EzySimpleJsonMapper.builder().build();
    }
    
    protected EzyStatistics newStatistics() {
        return new EzySimpleStatistics();
    }
    
    protected EzyManagers newManagers() {
        EzyManagers managers = EzyManagersImpl.builder().build();
        addManagers(managers);
        return managers;
    }
    
    protected void addManagers(EzyManagers managers) {
    }
    
    protected EzyServerControllers newControllers() {
        EzyServerControllers controllers = EzyServerControllersImpl.builder().build();
        addControllers(controllers);
        return controllers;
    }
    
    protected void addControllers(EzyServerControllers controllers) {
    }
    
    protected EzyRequestMappers newRequestMapper() {
        EzyRequestMappers mappers = EzyRequestMappersImpl.builder().build();
        addRequestMappers(mappers);
        return mappers;
    }
    
    protected void addRequestMappers(EzyRequestMappers mappers) {
    }
    
    protected EzyEventPluginsMapper newEventPluginsMapper(EzySettings settings) {
        return EzyEventPluginsMapperImpl.builder()
                .plugins(settings.getPlugins()).build();
    }
    
    protected Map<String, EzyAppClassLoader> newAppClassLoaders() {
        Map<String, EzyAppClassLoader> answer = new ConcurrentHashMap<>();
        for(File dir : getEntryFolders())
        	answer.put(dir.getName(), newAppClassLoader(dir));
        return answer;
    }
    
    protected EzyAppClassLoader newAppClassLoader(File dir) {
    	getLogger().info("load " + dir);
        return new EzyAppClassLoader(dir, classLoader);
    }
    
    protected File[] getEntryFolders() {
        File entries = getEntriesFolder();
        return entries.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
    }
    
    protected File getEntriesFolder() {
        String entriesPath = getEntriesPath();
        File entries = new File(entriesPath);
        return entries;
    }
    
    protected String getEntriesPath() {
        return getPath(getAppsPath(), EzyFolderNamesSetting.ENTRIES);
    }
    
    protected String getAppsPath() {
    	return getPath(getHomePath(), EzyFolderNamesSetting.APPS);
    }
    
    protected String getSettingsPath() {
    	return getPath(getHomePath(), EzyFolderNamesSetting.SETTINGS);
    }
    
    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }
    
    protected String getHomePath() {
    	return config.getEzyfoxHome();
    }
    
    public EzyLoader classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }
    
    public EzyLoader config(EzyConfig config) {
    	this.config = config;
    	return this;
    }
    
    
}
