package iae.home.datagenerator.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

@Root(name="LinearLayout")
public class UILinearLayout implements IUIControl {
	@Attribute(name="xmlns:android")
	private String ns="http://schemas.android.com/apk/res/android";
	
	@Attribute(name="android:orientation")
	private String mOrientation="vertical";
	
	@Attribute(name="android:layout_width")
	private String mWidth="match_parent";

	@Attribute(name="android:layout_height")
	private String mHeight="match_parent";

	public UILinearLayout() {}
	
	@ElementListUnion({
		@ElementList(name="TextView",inline=true,type=UITextView.class),
		@ElementList(name="EditText",inline=true,type=UIEditText.class),
		@ElementList(name="Spinner",inline=true,type=UISpinner.class),
		@ElementList(name="CheckBox",inline=true,type=UICheckBox.class),
		@ElementList(name="Button",inline=true,type=UIButton.class)
	})
	private List<IUIControl> mChilds;
	
	public List<IUIControl> getChilds() {
		if(mChilds==null)
			mChilds=new ArrayList<IUIControl>();
		
		return mChilds;
	}
}
