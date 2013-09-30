package iae.home.datagenerator.datamodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="CheckBox")
public class UICheckBox implements IUIControl {
	@Attribute(name="android:layout_width")
	private String mWidth="match_parent";

	@Attribute(name="android:layout_height")
	private String mHeight="wrap_content";

	@Attribute(name="android:id")
	private String mId;

	@Attribute(name="android:text")
	private String mText;
	
	public UICheckBox(String id,String text) {
		mId="@+id/"+id;
		mText="@string/"+text;
	}
}
