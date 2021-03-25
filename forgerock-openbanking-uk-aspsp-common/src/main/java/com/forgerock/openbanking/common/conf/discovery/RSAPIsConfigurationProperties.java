/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.common.conf.discovery;

import com.forgerock.openbanking.api.annotations.OBGroupName;
import com.forgerock.openbanking.api.annotations.OBReference;
import com.forgerock.openbanking.api.annotations.OpenBankingAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPI;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConfigurationProperties(prefix = "rs")
public class RSAPIsConfigurationProperties {

    public Map<OBReference, Boolean> apis = new HashMap<>();

    private Map<String, Boolean> versions = new HashMap<>();
    private Map<String, Map<OBReference, Boolean>> versionApiOverrides = new HashMap<>();

    /** Set of all controller methods that are not in discovery */
    private final ControllerEndpointBlacklist controllerEndpointBlacklist = new ControllerEndpointBlacklist();

    @Value("${rs-discovery.base-url}")
    private String rsBaseUrl;
    private String financialID;

    private Map<OBGroupName, Map<String, OBDiscoveryAPI>> discoveryApisByVersionAndGroupName = null;

    public ControllerEndpointBlacklist getControllerEndpointBlacklist() {
        return controllerEndpointBlacklist;
    }

    public String getFinancialID() {
        return financialID;
    }

    public void setFinancialID(String financialID) {
        this.financialID = financialID;
    }

    public Map<OBReference, Boolean> getApis() {
        return apis;
    }

    public void setApis(Map<OBReference, Boolean> apis) {
        this.apis = apis;
    }

    public Map<String, Boolean> getVersions() {
        return versions;
    }

    public void setVersions(Map<String, Boolean> versions) {
        this.versions = versions;
    }

    public Map<String, Map<OBReference, Boolean>> getVersionApiOverrides() {
        return versionApiOverrides;
    }

    public void setVersionApiOverrides(Map<String, Map<OBReference, Boolean>> versionApiOverrides) {
        this.versionApiOverrides = versionApiOverrides;
    }

    public String getRsBaseUrl() {
        return rsBaseUrl;
    }

    public boolean isAPIEnable(OBReference obReference) {
        return !getApis().containsKey(obReference) || getApis().get(obReference);
    }

    public boolean isVersionEnable(String version) {
        return !getVersions().containsKey(version) || getVersions().get(version);
    }

    public boolean isVersionOverrideEnable(String version, OBReference obReference) {
        // Use _ instead of . in yaml to reduce ambiguity over yml separator vs version
        String yamlVersion = version.replace(".", "-");
        if (versionApiOverrides.containsKey(yamlVersion)
                && versionApiOverrides.get(yamlVersion).containsKey(obReference)) {
            return versionApiOverrides.get(yamlVersion).get(obReference);
        }
        return true;
    }

    public Map<OBGroupName, Map<String, OBDiscoveryAPI>> getDiscoveryApisByVersionAndGroupName() {
        if (discoveryApisByVersionAndGroupName != null) {
            return discoveryApisByVersionAndGroupName;
        }

        Map<OBGroupName, Map<String, OBDiscoveryAPI>> unfilteredDiscoveryLinks = new HashMap<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(OpenBankingAPI.class, true, true));
        try {
            for (BeanDefinition bd : scanner.findCandidateComponents("com.forgerock")) {
                Class obRequestMappingClass = Class.forName(bd.getBeanClassName());
                List<Class> obInterfaces = interfacesWithOBAPIAnnotation(obRequestMappingClass);

                for (Class obInterface : obInterfaces) {
                    OpenBankingAPI classOpenBankingAPI = (OpenBankingAPI) obInterface.getAnnotation(OpenBankingAPI.class);
                    RequestMapping classRequestMapping = (RequestMapping) obInterface.getAnnotation(RequestMapping.class);

                    if (isVersionEnable(classOpenBankingAPI.obVersion())
                            && isAPIEnable(classOpenBankingAPI.obReference())
                            && isVersionOverrideEnable(classOpenBankingAPI.obVersion(), classOpenBankingAPI.obReference())) {
                        Method[] methods = obInterface.getMethods();
                        for (Method method : methods) {
                            OpenBankingAPI methodOpenBankingAPIAnnotation = method.getAnnotation(OpenBankingAPI.class);
                            RequestMapping methodRequestMappingAnnotation = method.getAnnotation(RequestMapping.class);

                            //Init map
                            if (!unfilteredDiscoveryLinks.containsKey(classOpenBankingAPI.obGroupName())) {
                                unfilteredDiscoveryLinks.put(classOpenBankingAPI.obGroupName(), new HashMap<>());
                            }
                            if (!unfilteredDiscoveryLinks.get(classOpenBankingAPI.obGroupName()).containsKey(classOpenBankingAPI.obVersion())) {
                                unfilteredDiscoveryLinks.get(classOpenBankingAPI.obGroupName()).put(classOpenBankingAPI.obVersion(), new OBDiscoveryAPI()
                                        .version("v" + classOpenBankingAPI.obVersion())
                                        .links(new GenericOBDiscoveryAPILinks())
                                );
                            }

                            //Add new discovery method
                            if (methodOpenBankingAPIAnnotation != null
                                    && isAPIEnable(methodOpenBankingAPIAnnotation.obReference())
                                    && isVersionOverrideEnable(classOpenBankingAPI.obVersion(), methodOpenBankingAPIAnnotation.obReference())
                            ) {
                                StringBuilder path = new StringBuilder(rsBaseUrl);
                                if (classRequestMapping.value().length > 0) {
                                    path.append(classRequestMapping.value()[0]);
                                }
                                if (methodRequestMappingAnnotation.value().length > 0) {
                                    path.append(methodRequestMappingAnnotation.value()[0]);
                                }
                                ((GenericOBDiscoveryAPILinks) unfilteredDiscoveryLinks
                                        .get(classOpenBankingAPI.obGroupName())
                                        .get(classOpenBankingAPI.obVersion())
                                        .getLinks())
                                        .addLink(methodOpenBankingAPIAnnotation.obReference().getReference(),
                                                path.toString());
                            } else {
                                controllerEndpointBlacklist.add(obRequestMappingClass, method);
                            }
                        }
                    } else {
                        Arrays.stream(obInterface.getMethods())
                                .forEach(method -> controllerEndpointBlacklist.add(obRequestMappingClass, method));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("Can't initialise discovery map", e);
        }

        // Filter any empty groups from results (because ASPSP cfg has switched off everything in a subgroup)
        // Must assign first before returning for lazy loading at start of method
        discoveryApisByVersionAndGroupName = filterEmptyMaps(unfilteredDiscoveryLinks);

        return discoveryApisByVersionAndGroupName;
    }

    public List<OBDiscoveryAPI> getObDiscoveryAPIS() {
        List<OBDiscoveryAPI> obDiscoveryAPIS = new ArrayList<>();
        getDiscoveryApisByVersionAndGroupName().values().forEach(m -> m.forEach((k, v) -> obDiscoveryAPIS.add(v)));
        return obDiscoveryAPIS;
    }

    private List<Class> interfacesWithOBAPIAnnotation(Class obRequestMappingClass) {
        List<Class> obInterfaces = new ArrayList<>();
        for (Class classInterface : obRequestMappingClass.getInterfaces()) {
            OpenBankingAPI a = (OpenBankingAPI) classInterface.getAnnotation(OpenBankingAPI.class);
            if (a != null) {
                obInterfaces.add(classInterface);
            }
        }
        return obInterfaces;
    }

    private static Map<OBGroupName, Map<String, OBDiscoveryAPI>> filterEmptyMaps(Map<OBGroupName, Map<String, OBDiscoveryAPI>> byGroups) {
        return byGroups.entrySet()
                .stream()
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> filterEmptyLinkMaps(e.getValue())));
    }

    private static Map<String, OBDiscoveryAPI> filterEmptyLinkMaps(Map<String, OBDiscoveryAPI> byLinks) {
        return byLinks.entrySet()
                .stream()
                .filter(e -> !noLinks(e))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static boolean noLinks(Map.Entry<String, OBDiscoveryAPI> value) {
        return noLinks((GenericOBDiscoveryAPILinks) value.getValue().getLinks());
    }

    private static boolean noLinks(GenericOBDiscoveryAPILinks links) {
        return links==null || links.getLinkValues().isEmpty() && links.getLinks().isEmpty();
    }

}
