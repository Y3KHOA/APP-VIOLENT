package com.example.appviolent;

import java.util.ArrayList;

public class Paginator {
    int TOTAL_NUM_ITEMS, ITEMS_PER_PAGE,ITEMS_REMAINING,LASTPAGE ;

    public Paginator(int TOTAL_NUM_ITEMS, int ITEMS_PER_PAGE) {
        this.TOTAL_NUM_ITEMS = TOTAL_NUM_ITEMS;
        this.ITEMS_PER_PAGE = ITEMS_PER_PAGE;
        this.ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        this.LASTPAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
    }

    public int getTOTAL_NUM_ITEMS() {
        return TOTAL_NUM_ITEMS;
    }

    public void setTOTAL_NUM_ITEMS(int TOTAL_NUM_ITEMS) {
        this.TOTAL_NUM_ITEMS = TOTAL_NUM_ITEMS;
    }

    public int getITEMS_PER_PAGE() {
        return ITEMS_PER_PAGE;
    }

    public void setITEMS_PER_PAGE(int ITEMS_PER_PAGE) {
        this.ITEMS_PER_PAGE = ITEMS_PER_PAGE;
    }

    public int getITEMS_REMAINING() {
        return ITEMS_REMAINING;
    }

    public void setITEMS_REMAINING(int ITEMS_REMAINING) {
        this.ITEMS_REMAINING = ITEMS_REMAINING;
    }

    public int getLASTPAGE() {
        return LASTPAGE;
    }

    public void setLASTPAGE(int LASTPAGE) {
        this.LASTPAGE = LASTPAGE;
    }

    public ArrayList<Video> generatePage(ArrayList<Video> arr_ogrinal ,int  indexCurrent)
    {
        if(arr_ogrinal.size()<=0) return  null;
        int startItem = indexCurrent*ITEMS_PER_PAGE +1 ;
        int endItem = (indexCurrent+1) * ITEMS_PER_PAGE;
        int i = 0 ;
        ArrayList<Video>  arr_cut = new ArrayList<Video>();
        for (int k = 0 ; k< arr_ogrinal.size(); k ++ ){
            if(k >= startItem && k <= endItem){
                arr_cut.add(arr_ogrinal.get(k));

            }
        }
        return arr_cut;
    }
}
