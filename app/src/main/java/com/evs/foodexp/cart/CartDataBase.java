package com.evs.foodexp.cart;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Cart.class},version = 1)
public abstract class CartDataBase extends RoomDatabase {

    private static CartDataBase instance;

    public abstract CartDao noteDao();

    public static synchronized CartDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CartDataBase.class, "note_database")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomcallback)
                    .build();

        }
        return instance;
    }

//    private static RoomDatabase.Callback roomcallback=new RoomDatabase.Callback(){
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(instance).execute();
//        }
//    };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
//        private NoteDao noteDao;
//        public PopulateDbAsyncTask(NoteDataBase db) {
//            noteDao=db.noteDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
////            noteDao.insert(new Note("satish","satish@gmails.com","584395849358"
////                    ,"fever","combiflame","ramo","ramms","","","",""));
//            return null;
//        }
//    }

}
