package com.yadev.listener;

import com.yadev.entity.Revision;
import org.hibernate.envers.RevisionListener;
public class YadevRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        //SecurityContext.getUser().getId()
        ((Revision) revisionEntity).setUsername("yadev");
    }
}
