package iae.home.datagenerator.datamodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="Spinner")
public class UISpinner implements IUIControl {
	@Attribute(name="android:layout_width")
	private String mWidth="match_parent";

	@Attribute(name="android:layout_height")
	private String mHeight="wrap_content";

	@Attribute(name="android:id")
	private String mId;

	public UISpinner(String id) {
		mId="@+id/"+id;
	}
}
