package com.interchecks.review.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.nonNull;

import java.util.Collection;

import lombok.experimental.UtilityClass;

@UtilityClass 

public class Util {

	public static final boolean isFalse(boolean b) { 
		return (!isTrue(b)); //Could as easily be "!b", but just for clarity and tie-in
	}
	
	public static final boolean isTrue(boolean b) { 
		return b;
	}
	
	public static final <T> Collection<T> safeCollection(Collection<T> inCollection) { 
		return (nonNull(inCollection) ? unmodifiableCollection(inCollection) : emptyList());
	}
	
}
