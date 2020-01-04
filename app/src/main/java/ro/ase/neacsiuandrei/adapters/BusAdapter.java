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
import ro.ase.neacsiuandrei.database.BusDao;
import ro.ase.neacsiuandrei.database.BusService;
import ro.ase.neacsiuandrei.database.DatabaseInstance;
import ro.ase.neacsiuandrei.models.Bus;

public class BusAdapter extends BaseAdapter {

    private Context context;
    public List<Bus> busList;

    public BusAdapter(Context context, List<Bus> busList){
        this.context=context;
        this.busList=busList;
    }

    public BusAdapter(Context context){
        this.context=context;
        this.busList=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return busList.size();
    }

    @Override
    public Object getItem(int i) {
        return busList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return busList.get(i).getNumber();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BusHolder busHolder;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.lv_items_bus, viewGroup, false);
            busHolder = new BusHolder(view);
            view.setTag(busHolder);
        }
        else{
            busHolder = (BusHolder)view.getTag();
        }

        Bus bus = (Bus)getItem(i);

        busHolder.tvNumber.setText(Integer.toString(bus.getNumber()));
        busHolder.tvCapacity.setText(Integer.toString(bus.getCapacity()));
        busHolder.tvAvgTime.setText(Float.toString(bus.getAvgDuration()));
        busHolder.rating.setRating(bus.getRating());
        busHolder.recommend.setChecked(bus.isRecommend());
        busHolder.recommend.setClickable(false);

        return view;
    }

    private class BusHolder{
        public TextView tvNumber;
        public TextView tvCapacity;
        public TextView tvAvgTime;
        public RatingBar rating;
        public Switch recommend;

        public BusHolder(View convertView){
            tvNumber = convertView.findViewById(R.id.tvNumber);
            tvCapacity = convertView.findViewById(R.id.tvCapacity);
            tvAvgTime = convertView.findViewById(R.id.tvAvgTime);
            rating = convertView.findViewById(R.id.rtgBus);
            recommend = convertView.findViewById(R.id.recommendBus);
        }
    }

    public void notifyDatabaseChanged(Bus bus) {
        DatabaseInstance db = DatabaseInstance.getInstance(context);
        BusDao busDao = db.getBusDao();
        final BusService busService = new BusService(busDao);
        busService.addBus(bus);
    }
}
