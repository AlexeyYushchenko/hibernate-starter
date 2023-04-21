    package com.yadev.util;

    import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
    import com.yadev.converter.BirthdayConverter;
    import com.yadev.entity.Audit;
    import com.yadev.entity.Revision;
    import com.yadev.entity.User;
    import com.yadev.listener.AuditTableListener;
    import lombok.experimental.UtilityClass;
    import org.hibernate.SessionFactory;
    import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
    import org.hibernate.cfg.Configuration;
    import org.hibernate.event.service.spi.EventListenerRegistry;
    import org.hibernate.event.spi.EventType;
    import org.hibernate.internal.SessionFactoryImpl;

    @UtilityClass
    public class HibernateUtil {
        public static SessionFactory buildSessionFactory(){
            Configuration configuration = buildConfiguration();
            configuration.configure();

            var sessionFactory = configuration.buildSessionFactory();
    //        registerListeners(sessionFactory);

            return sessionFactory;
        }

        private static void registerListeners(SessionFactory sessionFactory) {
            var sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
            var listenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
            var auditTableListener = new AuditTableListener();
            listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
            listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
        }

        public static Configuration buildConfiguration() {
            Configuration configuration = new Configuration();

            //TYPES, ANNOTATED CLASSES, (ATTRIBUTE) CONVERTERS, INTERCEPTORS, NAMING STRATEGIES (TAC A CI NS)
            configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

            //ADDING ALL CLASSES WE WANT HIBERNATE TO MAP
    // OR YOU CAN ADD CLASSES TO HIBERNATE.CFG.XML FILE
            configuration.addAnnotatedClass(Audit.class); //or add class to a cfg file.
            configuration.addAnnotatedClass(Revision.class); //or add class to a cfg file.
            configuration.addAnnotatedClass(User.class); //or add class to a cfg file.
    //        configuration.addAnnotatedClass(Company.class); //or add class to a cfg file.

            //ADDING ALL CUSTOM CONVERTERS
            configuration.addAttributeConverter(BirthdayConverter.class);

            //ADDING CUSTOMS TYPES IF PRESENT
            //IN OUR CASE WE HAVE JSONBI
            configuration.registerTypeOverride(new JsonBinaryType());

    //        configuration.setInterceptor(new GlobalInterceptor());
            return configuration;
        }

    }
