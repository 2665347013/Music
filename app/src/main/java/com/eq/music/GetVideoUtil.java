package com.eq.music;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;


public class GetVideoUtil {

    public ArrayList<Video> getVideo(ContentResolver contentResolver){
        ArrayList<Video> vlist = new ArrayList<>();
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,null,null,null);

        if (cursor.moveToFirst()){
            for (int i=0;i<cursor.getCount();i++){
                Video video = new Video();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                int isvideo = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));

                if (isvideo != 0){
                    video.setId(id);
                    video.setTitle(title);
                    video.setArtist(artist);
                    video.setAlbum(album);
                    video.setUrl(url);
                    video.setDuration(duration);
                    video.setSize(size);

                    Log.e("getVideo",title+"----------"+url);
                    vlist.add(video);
                }
                cursor.moveToNext();
            }
        }
        return vlist;
    }
}
