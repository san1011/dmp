package com.evt.dmp.protocal.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanItemRoot {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("error")
        @Expose
        private Object error;
        @SerializedName("response")
        @Expose
        private List<PlanItem> response = null;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Object getError() {
            return error;
        }

        public void setError(Object error) {
            this.error = error;
        }

        public List<PlanItem> getResponse() {
            return response;
        }

        public void setResponse(List<PlanItem> response) {
            this.response = response;
        }
}
