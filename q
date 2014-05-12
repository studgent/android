[1mdiff --git a/src/be/ugent/oomo/groep12/studgent/data/POIDataSource.java b/src/be/ugent/oomo/groep12/studgent/data/POIDataSource.java[m
[1mindex b88284e..bd4d08a 100644[m
[1m--- a/src/be/ugent/oomo/groep12/studgent/data/POIDataSource.java[m
[1m+++ b/src/be/ugent/oomo/groep12/studgent/data/POIDataSource.java[m
[36m@@ -51,7 +51,7 @@[m [mpublic class POIDataSource implements IDataSource {[m
 		items = new HashMap<Integer,IPointOfInterest>();[m
 		[m
 		try {[m
[31m-			String apidata =  CurlUtil.get("poi");[m
[32m+[m			[32mString apidata = CurlUtil.get("poi");[m
 			JSONArray poi_items = new JSONArray(apidata);[m
 			for (int i = 0; i < poi_items.length(); i++) {[m
 				JSONObject item = poi_items.getJSONObject(i);[m
