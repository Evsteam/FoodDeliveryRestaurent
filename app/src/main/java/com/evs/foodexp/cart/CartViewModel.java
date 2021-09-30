package com.evs.foodexp.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CartViewModel extends AndroidViewModel {

    private CartRepository repository;
    private LiveData<List<Cart>> allnotes;

    public CartViewModel(@NonNull Application application) {
        super(application);
        repository = new CartRepository(application);
        allnotes = repository.getAllNotes();
    }

    public Long insert(Cart note) {
       return repository.insert(note);

    }

    public void update(Cart note) {
        repository.update(note);
    }

    public void updateCart(String quantity,String ids) {
        try {
            repository.updateCart(quantity,ids);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Cart note) {

        repository.delete(note);
    }


    public Cart getNote(String NoteId) throws ExecutionException, InterruptedException {
        return repository.getNote(NoteId);
    }

    public LiveData<Cart> getliveData(String NoteId) {
        return repository.getliveNote(NoteId);
    }

    public LiveData<List<Cart>> getAllnotes() {
        return allnotes;
    }

//    public LiveData<List<Cart>> getMemberList(String type) {
//        return repository.getMember(type);
//    }


}

