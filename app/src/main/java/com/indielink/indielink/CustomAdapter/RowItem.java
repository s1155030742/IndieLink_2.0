package   com.indielink.indielink.CustomAdapter;

/**
 * Created by lawFuck on 2015/11/27.
 */
public class RowItem {
    private String text;
    private String imageUrl;

    public RowItem(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}