package com.cmput301w23t00.qrquest.ui.library;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cmput301w23t00.qrquest.MainActivity;
import com.cmput301w23t00.qrquest.R;
import com.cmput301w23t00.qrquest.databinding.FragmentLibraryBinding;
import com.cmput301w23t00.qrquest.ui.library.qrCodeSummaryStatistics.QrCodeSummaryStatisticsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.installations.FirebaseInstallations;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class LibraryFragment extends Fragment {
    private String userID;
    private long highestScore;
    private long lowestScore;
    private long sumOfScores;
    private long totalScanned;
    private FragmentLibraryBinding binding;
    private ListView QRList;
    private ArrayAdapter<LibraryQRCode> QRAdapter;
    private ArrayList<LibraryQRCode> dataList;
    FirebaseFirestore db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LibraryViewModel libraryViewModel =
                new ViewModelProvider(this).get(LibraryViewModel.class);
        Log.d("Loaded1", "222");
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // QR Code List
        db = FirebaseFirestore.getInstance();
        final CollectionReference userCollectionReference = db.collection("users");
        final CollectionReference usersQRCodesCollectionReference = db.collection("usersQRCodes");
        final CollectionReference qrcodesCollectionReference = db.collection("qrcodes");

        highestScore = 0;
        sumOfScores = 0;
        totalScanned = 0;
        lowestScore = -1;

        QRList = binding.libraryQrCodesList;
        dataList = new ArrayList<>();
        Log.d("userID", "0");
        //userID = FirebaseInstallations.getInstance().getId().toString();
        userID = "com.google.android.gms.tasks.zzw@b2bf36a";
        Log.d("userID", userID);
        QRAdapter = new LibraryQRCodeAdapter(getActivity(), dataList);
        QRList.setAdapter(QRAdapter);
        usersQRCodesCollectionReference.whereEqualTo("identifierID", userID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String qrCodeData = (String) document.getData().get("qrCodeData");
                                com.google.firebase.Timestamp timestamp = (com.google.firebase.Timestamp) document.getData().get("dateScanned");
                                Date dateScanned = timestamp.toDate();
                                qrcodesCollectionReference.whereEqualTo("qrCodeData", qrCodeData)
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Long score = (long) document.getData().get("score");
                                                        highestScore = score > highestScore ? score : highestScore;
                                                        if (lowestScore == -1) {
                                                            lowestScore = score;
                                                        } else {
                                                            lowestScore = score < lowestScore ? score : lowestScore;
                                                        }
                                                        sumOfScores += score;
                                                        totalScanned += 1;
                                                        dataList.add(new LibraryQRCode(qrCodeData, score, dateScanned));
                                                    }
                                                    QRAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
        //final TextView textView = binding.textLibrary;
        //libraryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        // QR Stats button
        Button viewQrStats = binding.viewPersonalQrStatsButton;
        viewQrStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("highestScore", highestScore);
                bundle.putLong("lowestScore", lowestScore);
                bundle.putLong("sumOfScores", sumOfScores);
                bundle.putLong("totalScanned", totalScanned);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_qrcode_library_to_qrCodeSummaryStatisticsFragment2, bundle);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}