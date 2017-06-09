package sg.com.innosys.wms.UI.GRN;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sg.com.innosys.wms.BLL.GRN.GrnDetail;
import sg.com.innosys.wms.BLL.GRN.GrnPut;
import sg.com.innosys.wms.R;
import sg.com.innosys.wms.UI.Common.ListViewItems;


public class FragUnbalanceGrnPut extends Fragment{
	private View vFragUnbalancePutting;
	private ListView lvList;
	private ArrayList<GrnPut> grnPutList;
	private ActUnbalanceGrnDetail actUnbalanceGrnDetail;
	private UnbalanceGrnPutAdapter adpGrnPutSearchAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//initialize here..
		actUnbalanceGrnDetail = (ActUnbalanceGrnDetail) getActivity();
		if(savedInstanceState != null){
			actUnbalanceGrnDetail.selectedGrnDetail = (GrnDetail) savedInstanceState.getSerializable(actUnbalanceGrnDetail.SELECTEDGRNDETAIL);
		}
		
	    refreshAdapter();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		vFragUnbalancePutting = inflater.inflate(R.layout.frag_unbalance_grnput, container, false);
		lvList = (ListView)vFragUnbalancePutting.findViewById(R.id.lvvUnbalancePutting);	
		return vFragUnbalancePutting;
	} 	
	 @Override
	 public void onSaveInstanceState(Bundle outState){
		 super.onSaveInstanceState(outState);
		 outState.putSerializable(actUnbalanceGrnDetail.SELECTEDGRNDETAIL, actUnbalanceGrnDetail.selectedGrnDetail);
	 }	
    @Override
    public void onResume(){
    	super.onResume();
    	actUnbalanceGrnDetail = (ActUnbalanceGrnDetail) getActivity();
    	refreshAdapter();
    }  
		
	public OnItemClickListener itemClick = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> adp, View view, int position,
				long arg3) {
			//grnPutsList = dbWhGrn.getAllGrnPut();
			GrnPut selectedPallet = (GrnPut) grnPutList.get(position);
			Toast.makeText(getActivity(), "Select Pallet is : \nPalletId - "
							+ selectedPallet.getPalletId()
							+ selectedPallet.getHasRack().toString(), 5).show();
		}		
	};
		
	public void refreshAdapter()
	 {
		grnPutList = new ArrayList<GrnPut>();
		if(actUnbalanceGrnDetail.selectedGrnDetail != null){
			if(actUnbalanceGrnDetail.selectedGrnDetail.grnPuts.size() >0){
				grnPutList = actUnbalanceGrnDetail.selectedGrnDetail.grnPuts;				
			}
		}		
		
		Comparator<GrnPut> comparebyPalletId = GrnDetail.sortByPalletId;
		Collections.sort(grnPutList,comparebyPalletId);
		
		adpGrnPutSearchAdapter = new UnbalanceGrnPutAdapter(getActivity(), R.layout.row_unbalanceputtinglist, grnPutList );
		lvList.setAdapter(adpGrnPutSearchAdapter); 		
		adpGrnPutSearchAdapter.notifyDataSetChanged();
	 }	
	public class UnbalanceGrnPutAdapter extends ArrayAdapter<GrnPut> {
		 ArrayList<GrnPut> mGrnPutList;

	    	public UnbalanceGrnPutAdapter(Context context, int textViewResourceId,
	    			ArrayList<GrnPut> grnPutList) {
	             super(context, textViewResourceId, grnPutList);
	             mGrnPutList = grnPutList;
	 
	         }
	    	
			public View getView(int position, View convertView, ViewGroup parent) {
	              View v = convertView;
	              if (v == null) {

	                  LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                  v = vi.inflate(R.layout.row_unbalanceputtinglist, null);	                  
	              }
	              
	              GrnPut item = mGrnPutList.get(position);
	              
	              if (item != null) {
	            	  ListViewItems palletIdView = (ListViewItems) v.findViewById(R.id.row_unbalanceput_PalletID);
	            	  ListViewItems zoneView = (ListViewItems) v.findViewById(R.id.row_unbalanceput_Zone);
	            	  ListViewItems rowView = (ListViewItems) v.findViewById(R.id.row_unbalanceput_Row);
	            	  ListViewItems columnView = (ListViewItems) v.findViewById(R.id.row_unbalanceput_Column);
	            	  ListViewItems tierView = (ListViewItems) v.findViewById(R.id.row_unbalanceput_Tier);
	            	  ListViewItems actQtyView = (ListViewItems) v.findViewById(R.id.row_unbalanceput_ActQty);
 	  
	            	  
	                  if(palletIdView != null){
	                	  palletIdView.setText(item.getPalletId());
	                  }
	                  if(zoneView != null){
	                	  zoneView.setText(item.getZone());
	                  }
	                  if(rowView != null){
	                	  rowView.setText(item.getRow());
	                  }
	                  if(columnView != null){
	                	  columnView.setText(item.getColumn());
	                  }    	   
	                  if(tierView != null){
	                	  tierView.setText(item.getTier());
	                  }    	 
	                  if(actQtyView != null){
	                	  actQtyView.setText(Integer.toString(item.getActQty()));
	                  }    	               
	              }
	              
	              return v;
	          }

			@Override
			public int getCount() {
				return super.getCount();
			} 
			@Override
			public GrnPut getItem(int position){
				return mGrnPutList.get(position);
			}

			 
	    }
}
