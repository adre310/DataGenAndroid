package iae.home.datagenerator.datamodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="TextView")
public class UITextView implements IUIControl {

	@Attribute(name="android:layout_width")
	private String mWidth="wrap_content";

	@Attribute(name="android:layout_height")
	private String mHeight="wrap_content";
	
	@Attribute(name="android:text")
	private String mText="match_parent";
	
	public UITextView(String text) {
		mText="@string/"+text;
	}
}
