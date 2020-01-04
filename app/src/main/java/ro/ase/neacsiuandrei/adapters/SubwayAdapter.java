package ro.ase.neacsiuandrei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.ase.neacsiuandrei.R;
import ro.ase.neacsiuandrei.database.DatabaseInstance;
import ro.ase.neacsiuandrei.database.SubwayDao;
import ro.ase.neacsiuandrei.database.SubwayService;
import ro.ase.neacsiuandrei.models.Subway;

public class SubwayAdapter extends BaseAdapter {

    private Context context;
    public List<Subway> subwayList;

    public SubwayAdapter(Context cx, List<Subway> subList){
        this.context=cx;
        this.subwayList=subList;
    }

    public SubwayAdapter(Context cx){
        this.context=cx;
        this.subwayList=new ArrayList<>();
    }


    @Override
    public int getCount() {
        return subwayList.size();
    }

    @Override
    public Object getItem(int position) {
        return subwayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SubwayHolder subHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_items_subway, parent, false);
            subHolder = new SubwayHolder(convertView);
            convertView.setTag(subHolder);
        }
        else{
            subHolder = (SubwayHolder) convertView.getTag();
        }

        Subway subway = (Subway)getItem(position);

        subHolder.tvDestination.setText(subway.getDestination());
        subHolder.tvCapacity.setText(Integer.toString(subway.getCapacity()));
        subHolder.tvSubwayLine.setText(subway.getSubwayLine());
        subHolder.tvNoStations.setText(Integer.toString(subway.getNumberOfStations()));
        subHolder.ratingBar.setRating(subway.getRating());
        subHolder.recommend.setChecked(subway.isRecommend());
        subHolder.recommend.setClickable(false);

        return convertView;
    }

    public class SubwayHolder{
        public TextView tvDestination;
        public TextView tvCapacity;
        public TextView tvSubwayLine;
        public TextView tvNoStations;
        public RatingBar ratingBar;
        public Switch recommend;


        public SubwayHolder(View convertView){
            tvDestination = convertView.findViewById(R.id.tvDestination);
            tvCapacity = convertView.findViewById(R.id.tvCapacity);
            tvSubwayLine = convertView.findViewById(R.id.tvSubLine);
            tvNoStations = convertView.findViewById(R.id.tvNoStations);
            ratingBar = convertView.findViewById(R.id.ratingListView);
            recommend = convertView.findViewById(R.id.switchListView);
        }
    }

    public void notifyDatabaseChanged(Subway subway) {
        DatabaseInstance db = DatabaseInstance.getInstance(context);
        SubwayDao subwayDao = db.getSubwayDao();
        final SubwayService subwayService = new SubwayService(subwayDao);
        subwayService.addSubway(subway);
    }
}
