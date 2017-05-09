package com.lee.xqq.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lee.xqq.R;
import com.lee.xqq.fragment.HomeFragment;

public class FriendAdapter extends BaseExpandableListAdapter {

	private List<RosterGroup> groups;
	private int groupResID;
	private int childResID;
	protected LayoutInflater inflater;
	private String offline;

	public FriendAdapter(Context context, Collection<RosterGroup> groups,
			int groupResID, int childResID) {
		super();
		this.groups = new ArrayList<RosterGroup>();
		this.groups.addAll(groups);
		this.inflater = LayoutInflater.from(context);
		this.groupResID = groupResID;
		this.childResID = childResID;
		offline = context.getResources().getString(
				R.string.string_status_offline);
	}

	public List<RosterGroup> getGroups() {
		return groups;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition).getEntries().size();
	}

	@Override
	public RosterGroup getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	@Override
	public RosterEntry getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		List<RosterEntry> list = (List<RosterEntry>) groups.get(groupPosition)
				.getEntries();
		return list.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupViewHolder holder = null;
		if (convertView == null) {
			holder = new GroupViewHolder();
			convertView = inflater.inflate(groupResID, null);
			holder.groupName = (TextView) convertView
					.findViewById(R.id.group_name);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		holder.groupName.setText(getGroup(groupPosition).getName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildViewHolder holder = null;
		if (convertView == null) {
			holder = new ChildViewHolder();
			convertView = inflater.inflate(childResID, null);
			holder.childText = (TextView) convertView
					.findViewById(R.id.child_text);
			holder.childStatus = (TextView) convertView
					.findViewById(R.id.child_status);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		RosterEntry entry = getChild(groupPosition, childPosition);
		holder.childText.setText(entry.getName());
		Presence presence = HomeFragment.getPresence(entry.getUser());
		if (presence == null)
			holder.childStatus.setText(offline);
		else
			holder.childStatus.setText(presence.getStatus());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	class GroupViewHolder {
		TextView groupName;
	}

	class ChildViewHolder {
		TextView childText;
		TextView childStatus;
	}

}
