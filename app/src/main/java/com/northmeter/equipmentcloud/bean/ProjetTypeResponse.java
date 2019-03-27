package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/3/12.
 * 产品型号列表
 */

public class ProjetTypeResponse extends CommonResponse {
    private List<PageList> list;

    public List<PageList> getList() {
        return list;
    }

    public void setList(List<PageList> list) {
        this.list = list;
    }

    public class PageList implements Serializable {
        private int recordId;
        private String itemType;
        private String bigCategoryName;
        private String smallCategoryName;

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getBigCategoryName() {
            return bigCategoryName;
        }

        public void setBigCategoryName(String bigCategoryName) {
            this.bigCategoryName = bigCategoryName;
        }

        public String getSmallCategoryName() {
            return smallCategoryName;
        }

        public void setSmallCategoryName(String smallCategoryName) {
            this.smallCategoryName = smallCategoryName;
        }
    }
}
