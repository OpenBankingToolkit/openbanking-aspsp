/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.conf.data;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConfigurationProperties(prefix = "data-template")
/**
 * The POJO representing the JWKms configuration
 */
public class DataConfigurationProperties {

    private String defaultProfile;
    private List<DataTemplateProfile> profiles;

    public String getDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(String defaultProfile) {
        this.defaultProfile = defaultProfile;
    }

    public List<DataTemplateProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<DataTemplateProfile> profiles) {
        this.profiles = profiles;
    }

    public static class DataTemplateProfile {
        private String id;
        private String name;
        private String description;
        private Resource template;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Resource getTemplate() {
            return template;
        }

        public void setTemplate(Resource template) {
            this.template = template;
        }
    }

}
