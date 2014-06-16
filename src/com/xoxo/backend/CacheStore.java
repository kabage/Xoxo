package com.xoxo.backend;

import java.util.ArrayList;

public class CacheStore {

	public static ArrayList<String> cachedNames;
	public static ArrayList<String> cachedJids;
	public static ArrayList<String> cachedStatuses;

	public static void initialize() {
		cachedNames = new ArrayList<String>();
		cachedJids = new ArrayList<String>();
		cachedStatuses = new ArrayList<String>();

	}

}
