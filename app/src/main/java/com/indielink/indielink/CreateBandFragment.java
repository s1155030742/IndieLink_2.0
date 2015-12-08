package com.indielink.indielink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.indielink.indielink.Network.HttpPost;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * Created by Hong on 16/11/2015.
 */
public class CreateBandFragment extends Fragment {

    TextView textTargetUri;
    ImageView targetImage;
    private String selectedImagePath;
    private ImageView img;

    public CreateBandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_band, container, false);
        final MediaPlayer player1 = MediaPlayer.create(getActivity(), R.raw.track1rumine);
        final MediaPlayer player2 = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
        final MediaPlayer player3 = MediaPlayer.create(getActivity(), R.raw.track3smokeonthewater);
        final MediaPlayer player4 = MediaPlayer.create(getActivity(), R.raw.track4takeiteasy);
        final MediaPlayer player5 = MediaPlayer.create(getActivity(), R.raw.track5thislove);
        final MediaPlayer player6 = MediaPlayer.create(getActivity(), R.raw.track6masterofpuppets);
        final MediaPlayer player7 = MediaPlayer.create(getActivity(), R.raw.track7hittheroadjack);
        final MediaPlayer player8 = MediaPlayer.create(getActivity(), R.raw.track8bringitonhome);
        final MediaPlayer player9 = MediaPlayer.create(getActivity(), R.raw.track9goodbye);

        //play track 1
        Button play1 = (Button) view.findViewById(R.id.playtrack1button);
        play1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player1.start();
            }
        });
        //stop track1
        Button stop1 = (Button) view.findViewById(R.id.stoptrack1button);
        stop1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player1.pause();
                player1.seekTo(player1.getCurrentPosition());
            }
        });

        //play track 2
        Button play2 = (Button) view.findViewById(R.id.playtrack2button);
        play2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
                player2.start();
            }
        });
        //stop track2
        Button stop2 = (Button) view.findViewById(R.id.stoptrack2button);
        stop2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player2.pause();
                player2.seekTo(player2.getCurrentPosition());
            }
        });

        //play track 3
        Button play3 = (Button) view.findViewById(R.id.playtrack3button);
        play3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
                player3.start();
            }
        });
        //stop track3
        Button stop3 = (Button) view.findViewById(R.id.stoptrack3button);
        stop3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player3.pause();
                player3.seekTo(0);
            }
        });

        //play track 4
        Button play4 = (Button) view.findViewById(R.id.playtrack4button);
        play4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
                player4.start();
            }
        });
        //stop track4
        Button stop4 = (Button) view.findViewById(R.id.stoptrack4button);
        stop4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player4.pause();
                player4.seekTo(0);
            }
        });

        //play track 5
        Button play5 = (Button) view.findViewById(R.id.playtrack5button);
        play5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
                player5.start();
            }
        });
        //stop track5
        Button stop5 = (Button) view.findViewById(R.id.stoptrack5button);
        stop5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player5.pause();
                player5.seekTo(0);
            }
        });

        //play track 6
        Button play6 = (Button) view.findViewById(R.id.playtrack6button);
        play6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
                player6.start();
            }
        });
        //stop track6
        Button stop6 = (Button) view.findViewById(R.id.stoptrack6button);
        stop6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player6.pause();
                player6.seekTo(0);
            }
        });

        //play track7
        Button play7 = (Button) view.findViewById(R.id.playtrack7button);
        play7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
                player7.start();
            }
        });
        //stop track7
        Button stop7 = (Button) view.findViewById(R.id.stoptrack7button);
        stop7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player7.pause();
                player7.seekTo(0);
            }
        });

        //play track 8
        Button play8 = (Button) view.findViewById(R.id.playtrack8button);
        play8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
                player8.start();
            }
        });
        //stop track8
        Button stop8 = (Button) view.findViewById(R.id.stoptrack8button);
        stop8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player8.pause();
                player8.seekTo(0);
            }
        });

        //play track 9
        Button play9 = (Button) view.findViewById(R.id.playtrack9button);
        play9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.track2getlucky);
                player9.start();
            }
        });
        //stop track9
        Button stop9 = (Button) view.findViewById(R.id.stoptrack9button);
        stop9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), R.raw.track1rumine);
                player9.pause();
                player9.seekTo(0);
            }
        });

        //load local image
        Button buttonLoadImage = (Button) view.findViewById(R.id.loadimage);
        textTargetUri = (TextView) view.findViewById(R.id.targeturi);
        targetImage = (ImageView) view.findViewById(R.id.targetimage);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
        final Button button = (Button) view.findViewById(R.id.SubmitNewBand);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO HTTP POST　Create Band Info.
                /*
                JSONObject js = new JSONObject();

                HttpPost httpPost = new HttpPost();
                httpPost.PostJSON("137.189.97.88:8080/band/add",js);
                */
                ((RootPage) getActivity()).reload();
            }
        });
        return view;
    }

    /**@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if  (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.targetimage);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }**/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}




