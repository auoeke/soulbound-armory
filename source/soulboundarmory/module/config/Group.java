package soulboundarmory.module.config;

import java.util.Locale;
import soulboundarmory.util.Util;

public class Group extends Parent {
    public final Parent parent;

    public Group(Parent parent, Class<?> type) {
        super(type, type.getSimpleName().toLowerCase(Locale.ROOT), Util.value(type, (Category category) -> {
            if (!ConfigurationFile.class.isAssignableFrom(type.getDeclaringClass())) {
                throw new IllegalArgumentException("@Category found on 2+ level nested " + type);
            }

            return category.value();
        }, parent.category));

        this.parent = parent;
    }
}