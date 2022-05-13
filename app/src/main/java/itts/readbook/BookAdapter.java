package itts.readbook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    //creating variables for arraylist and context.
    private ArrayList<BookInfo> bookInfoArrayList;
    private Context mcontext;

    //creating constructor for array list and context.
    public BookAdapter(ArrayList<BookInfo> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        //setting data to each UI component inside on bind view holder method.
        BookInfo bookInfo = bookInfoArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + bookInfo.getPageCount());
        holder.dateTV.setText(bookInfo.getPublishedDate());
        //below line is to change image link from http to https
        //String aUrl = bookInfo.getThumbnail().getImage_url().replace("http", "https");
        //below line is use to set image from URL in image view.
        Picasso.get().load(bookInfo.getThumbnail()).into(holder.bookIV);

        //below line is use to add on click listener for item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inside on click listener method calling a new activity and passing all the data
                //of that item in next intent.
                Intent i = new Intent(mcontext, itts.readbook.BookDetails.class);
                i.putExtra("title", bookInfo.getTitle());
                i.putExtra("subtitle", bookInfo.getSubtitle());
                //i.putExtra("authors", bookInfo.getAuthors());
                i.putExtra("publisher", bookInfo.getPublisher());
                i.putExtra("publishedDate", bookInfo.getPublishedDate());
                i.putExtra("description", bookInfo.getDescription());
                i.putExtra("pageCount", bookInfo.getPageCount());
                i.putExtra("thumbnail", bookInfo.getThumbnail());
                i.putExtra("previewLink", bookInfo.getPreviewLink());
                i.putExtra("infoLink", bookInfo.getInfoLink());
                i.putExtra("buyLink", bookInfo.getBuyLink());
                //after passing that data then starting new intent.
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {

        //returning the size of array list inside get item count method.
        return bookInfoArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        //below line is use to initialize text view and image views.
        TextView nameTV, publisherTV, pageCountTV, dateTV;
        ImageView bookIV;

        public BookViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVpublisher);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVbook);

        }
    }
}
