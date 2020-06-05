package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;

public class PopUp_RecyclerViewAdapter extends RecyclerView.Adapter<PopUp_RecyclerViewAdapter.ViewHolder> {
    private List<PopUpClass> popUps;
    private Context context;
    //private final ButtonClickInterface mOnClickListener;

    public interface ButtonClickInterface
    {
        void buttonClicked();
    }
    private final PopUp_RecyclerViewAdapter.ButtonClickInterface mOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder { /*implements View.OnClickListener*/
        public final TextView popupText;
        public final Button button;
        ViewHolder(View v) {
            super(v);
            popupText = v.findViewById(R.id.text_result);
            button = v.findViewById(R.id.forward_next_game);
        }

        /*Override
        public void onClick(View v) {
            PopUpClass.buttonClicked(button);
        }*/

        /*private void showPopupWindow(View view){
            //PopupWindow popupWindow = new PopupWindow(view.getContext());//
            //Create a View object through inflater
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
            final View popupView = inflater.inflate(R.layout.activity_popup, null);

            //Make Inactive Items Outside Of PopupWindow
            boolean focusable = false;

            final PopupWindow popupWindow = new PopupWindow(popupView, view.getWidth(), view.getHeight() ,focusable);

            //Initialize the elements of our window, install the handler
            //TextView result = popupView.findViewById(R.id.Text_Result);
            //Button buttonForward = popupView.findViewById(R.id.forward_next_game);

            //Set the location of the window on the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }*/
    }
    @Override
    public int getItemCount() {
        return popUps.size();
    }

    public void add(int position, PopUpClass item) {
        popUps.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        popUps.remove(position);
        notifyItemRemoved(position);
    }

    public PopUp_RecyclerViewAdapter(List<PopUpClass> popUps, Context context, ButtonClickInterface mOnClickListener) {
        this.popUps = popUps;
        this.context = context;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public PopUp_RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());

        View v = inflater.inflate(R.layout.popup_test, parent, false);
        return new PopUp_RecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PopUp_RecyclerViewAdapter.ViewHolder holder, final int position) {
        final PopUpClass popUp = popUps.get(position);
        System.out.println(holder.button);
        //holder.button.setText(popUp.content);
        //holder.popupText.setText(popUp.content);
        /*if (popUp.type == PopUpClass.PopupTypes.rightPopup){
            holder.button.setText(popUp.content);
        }
        else if (popUp.type == PopUpClass.PopupTypes.falsePopup){
            //int imageId = context.getResources().getIdentifier(popUp.content, "drawable", context.getPackageName());
            //holder.button.setBackgroundResource(imageId);
            holder.button.setText("");
        }

        //holder.popUpText.setText(popUp.getPopUpText());
        //holder.buttonForward = popUp.getForwardButton();
        //holder.buttonForward = popUp.getForwardButton();

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mOnClickListener.buttonClicked();
            }

        });
    }*/



    /*public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener {
        public TextView popUpText;
        public Button buttonForward;

        public ViewHolder(View itemView) {
            super(itemView);
            popUpText = (TextView) itemView.findViewById(R.id.text_result);
            buttonForward = (Button) itemView.findViewById(R.id.forward_next_game);
        }
    }*/
    }
}