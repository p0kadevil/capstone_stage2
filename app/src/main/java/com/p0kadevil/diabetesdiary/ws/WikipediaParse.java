package com.p0kadevil.diabetesdiary.ws;


import com.google.gson.annotations.SerializedName;

public class WikipediaParse {

    @SerializedName("title")
    public String title;

    @SerializedName("pageid")
    public int pageId;

    @SerializedName("text")
    public WikipediaText text;
}
