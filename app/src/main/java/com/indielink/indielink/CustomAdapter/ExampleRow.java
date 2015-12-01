package   com.indielink.indielink.CustomAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.indielink.indielink.R;
import com.squareup.picasso.Picasso;

/**
 * Created by lawFuck on 2015/11/27.
 */
public class ExampleRow  {
    // This is a reference to the layout we defined above
    public static final int LAYOUT = R.layout.item;
    private final Context context;
    private final TextView textView;
    private final ImageView imageView;

    ExampleRow(Context context, View convertView) {
        this.context = context;
        this.imageView = (ImageView) convertView.findViewById(R.id.BandImageView);
        this.textView = (TextView) convertView.findViewById(R.id.BandTextView);
    }

    public void bind(RowItem exampleViewModel) {
        this.textView.setText(exampleViewModel.getText());
        //TODO add URL inside load()
        Picasso.with(this.context).load(exampleViewModel.getImageUrl()).into(this.imageView);
    }
}
