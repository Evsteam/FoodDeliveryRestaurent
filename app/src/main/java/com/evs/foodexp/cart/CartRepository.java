package com.evs.foodexp.cart;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CartRepository {

    private CartDao noteDao;
    private LiveData<List<Cart>> allNotes;

    CartRepository(Application application) {
        CartDataBase noteDataBase = CartDataBase.getInstance(application);
        noteDao = noteDataBase.noteDao();
        allNotes = noteDao.getAllData();
    }

    public Long insert(Cart note) {

        try {
            return new InsertNoteAsyncTask(noteDao).execute(note).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1L;
//        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void updateCart(final String quantity, final String noteId)
        throws ExecutionException, InterruptedException {

            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    noteDao.updateCart(quantity,noteId);
                    return"";
                }
            };

            Future<String> future = Executors.newSingleThreadExecutor().submit(callable);


    }

    public void update(Cart note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Cart note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<Cart>> getAllNotes() {
        return allNotes;
    }

    public LiveData<Cart> getliveNote(String noteId) {
        return noteDao.getliveNote(noteId);
    }

    public Cart getNote(final String noteId)
//      return noteDao.getupdatedData(noteId);
      throws ExecutionException, InterruptedException {

            Callable<Cart> callable = new Callable<Cart>() {
                @Override
                public Cart call() throws Exception {
                    return noteDao.getupdatedData(noteId);
                }
            };

            Future<Cart> future = Executors.newSingleThreadExecutor().submit(callable);

            return future.get();
    }

//    public LiveData<List<Cart>> getMember(String type) {
//        return noteDao.getMemberList(type);
//    }



    private static class InsertNoteAsyncTask extends AsyncTask<Cart, Void, Long> {
        private CartDao noteDao;
        Long insertId = -1L;

        public InsertNoteAsyncTask(CartDao noteDao) {
            this.noteDao = noteDao;

        }

        @Override
        protected Long doInBackground(Cart... notes) {
            insertId = noteDao.insert(notes[0]);
            return insertId;
//            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Cart, Void, Void> {
        private CartDao noteDao;

        public UpdateNoteAsyncTask(CartDao noteDao) {
            this.noteDao = noteDao;

        }

        @Override
        protected Void doInBackground(Cart... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Cart, Void, Void> {
        private CartDao noteDao;

        public DeleteNoteAsyncTask(CartDao noteDao) {
            this.noteDao = noteDao;

        }

        @Override
        protected Void doInBackground(Cart... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

}
