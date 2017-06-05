package developers.sd.notes;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.UUID;

/**
 * Created by sohamdeshmukh on 25/05/17.
 */

public class SaveDialog extends DialogFragment {

    private static final String ARG_SAVE_NOTE = "save";
    public static final String EXTRA_SAVE_NOTE = "com.sd.android.note.save";

    public SaveDialog newInstance(UUID Id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SAVE_NOTE, Id);
        SaveDialog fragment = new SaveDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final UUID Id = (UUID) getArguments().getSerializable(ARG_SAVE_NOTE);


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.save_note)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note c = new Note(Id);
                                sendResult(Activity.RESULT_OK, true);
                                getActivity().finish();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note c = new Note(Id);
                                sendResult(Activity.RESULT_CANCELED, false);
                                getActivity().finish();
                            }
                        })
                .create();
    }


    private void sendResult(int resultCode, Boolean saved) {
        if (getTargetFragment() == null) {
            return; }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SAVE_NOTE, saved);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }



}
