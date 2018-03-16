package apps.lnsel.com.vhortexttest.data;

import java.io.Serializable;


public class GroupDataShareImage implements Serializable {


    public boolean isMasked = false;
    public String caption = "";
    public String imgUrl = "";
    public String imgUrlBlur = "";
    public boolean sketchType = false;

    public GroupDataShareImage() {
    }

    public String getImgUrlBlur() {
        return imgUrlBlur;
    }

    public void setImgUrlBlur(String imgUrlBlur) {
        this.imgUrlBlur = imgUrlBlur;
    }

    public boolean isMasked() {
        return isMasked;
    }

    public void setIsMasked(boolean isMasked) {
        this.isMasked = isMasked;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isSketchType() {
        return sketchType;
    }

    public void setSketchType(boolean sketchType) {
        this.sketchType = sketchType;
    }


}
