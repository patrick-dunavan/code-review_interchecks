package com.interchecks.review.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.function.Supplier;

import org.slf4j.Logger;

import lombok.experimental.UtilityClass;

@UtilityClass 

public class Util {

	public static final boolean isTrue(boolean b) { 
		return b;
	}

	public static final boolean isFalse(boolean b) { 
		return (!isTrue(b)); //Could as easily be "!b", but just for clarity and tie-in
	}
	
	public static final boolean isNot(boolean b) { 
		return (isFalse(b));
	}
	
	
	public static final <T> Collection<T> safeCollection(Collection<T> inCollection) { 
		return (nonNull(inCollection) ? unmodifiableCollection(inCollection) : emptyList());
	}
	
	
	public static final String fmtStr(String s, Object...args) { 
		return String.format(s, args);
	}
	
	
	public static final void debugIf(Logger log, Supplier<String> messageBuilder) {
		
		if (log.isDebugEnabled()) { 
			log.debug(messageBuilder.get());
		}
		
	}

	public static final void warnIf(Logger log, Supplier<String> messageBuilder) {
		
		if (log.isWarnEnabled()) { 
			log.warn(messageBuilder.get());
		}
	}

	public static final void errorIf(Logger log, Supplier<String> messageBuilder) {
		
		if (log.isErrorEnabled()) { 
			log.error(messageBuilder.get());
		}
		
	}

	public static final void infoIf(Logger log, Supplier<String> messageBuilder) {
		
		if (log.isInfoEnabled()) { 
			log.info(messageBuilder.get());
		}
		
	}
	
	
}
