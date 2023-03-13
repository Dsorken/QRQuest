package com.cmput301w23t00.qrquest.ui.profile;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfile {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String aboutMe;
    private static String phoneNumber;
    private static String email;
    private static String name;
    private static String userId;
    private static Boolean firstInstantiation = true;
    private static Boolean created = false;

    public UserProfile() {
        //only occurs when app is initially opened
        if (!firstInstantiation) {
            this.db.collection("users").whereEqualTo("identifierId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        aboutMe = task.getResult().getDocuments().get(0).getString("aboutMe");
                        phoneNumber = task.getResult().getDocuments().get(0).getString("phoneNumber");
                        email = task.getResult().getDocuments().get(0).getString("email");
                        name = task.getResult().getDocuments().get(0).getString("name");
                        firstInstantiation = false;
                        created = true;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                }
            });
        }
    }

    public static String getAboutMe() {
        return aboutMe;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static String getEmail() {
        return email;
    }

    public static String getName() {
        return name;
    }

    public static String getUserId() {
        return userId;
    }

    public static Boolean getCreated() {
        return created;
    }

    public static Boolean getFirstInstantiation() {
        return firstInstantiation;
    }

    public void setAboutMe(String aboutMe) {
        UserProfile.aboutMe = aboutMe;

        db.collection("users").whereEqualTo("identifierId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                DocumentReference documentReference = documentSnapshot.getReference();
                documentReference.update("aboutMe", aboutMe);
            }
        });
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

        db.collection("users").whereEqualTo("identifierId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                DocumentReference documentReference = documentSnapshot.getReference();
                documentReference.update("phoneNumber", phoneNumber);
            }
        });
    }

    public void setEmail(String email) {
        UserProfile.email = email;

        db.collection("users").whereEqualTo("identifierId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                DocumentReference documentReference = documentSnapshot.getReference();
                documentReference.update("email", email);
            }
        });
    }

    public void setName(String name) {
        UserProfile.name = name;

        db.collection("users").whereEqualTo("identifierId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                DocumentReference documentReference = documentSnapshot.getReference();
                documentReference.update("name", name);
            }
        });
    }

    public static void setUserId(String userId) {
        UserProfile.userId = userId;
    }

    public void setUserId2(String userId) {
        db.collection("users").whereEqualTo("identifierId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                DocumentReference documentReference = documentSnapshot.getReference();
                documentReference.update("identifierId", userId);
            }
        });
    }

    public static void setCreated(Boolean created) {
        UserProfile.created = created;
    }
}
