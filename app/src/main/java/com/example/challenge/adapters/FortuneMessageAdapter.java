package com.example.challenge.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.challenge.R;
import com.example.challenge.model.FortuneMessage;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FortuneMessageAdapter extends RecyclerView.Adapter<FortuneMessageAdapter.ViewHolder> {
    private List<FortuneMessage> fortuneMessageList;

    public  FortuneMessageAdapter(List<FortuneMessage> messages){
        fortuneMessageList = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View  view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fortune_list_messages, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FortuneMessage fortuneMessage = fortuneMessageList.get(position);

        //pick each of the message in this set and display with newline
        List<String> messages = fortuneMessage.getFortune();

        String messageToDisplay = "";

        for (String msg :
                messages) {
            messageToDisplay += msg + "\n";
        }

        holder.fortuneMessage.setText(messageToDisplay);
    }

    @Override
    public int getItemCount() {
        return fortuneMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fortuneMessage;


        public ViewHolder(View view) {
            super(view);
            fortuneMessage = view.findViewById(R.id.fortune_message);


        }
    }
}
