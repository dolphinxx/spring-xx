package com.jagsii.springxx.common.security.access;

import com.jagsii.springxx.common.security.access.annotation.HasPerm;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.annotation.AnnotationMetadataExtractor;

import java.util.Collection;
import java.util.Collections;

public class HasPermAnnotationMetadataExtractor implements AnnotationMetadataExtractor<HasPerm> {
    @Override
    public Collection<? extends ConfigAttribute> extractAttributes(HasPerm securityAnnotation) {
        String perm = securityAnnotation.value();
        return Collections.singletonList(new HasPermSecurityConfig(perm));
    }
}
