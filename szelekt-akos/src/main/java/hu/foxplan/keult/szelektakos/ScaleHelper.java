package hu.foxplan.keult.szelektakos;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Layout-ok méretezését kiszolgáló osztály
 */

public class ScaleHelper {

    // Scales the contents of the given view so that it completely fills the given
// container on one axis (that is, we're scaling isotropically).
    public static void scaleContents(View rootView, View container, boolean aspectRatio) {
        // Compute the scaling ratio
        float xScale = (float) container.getWidth() / rootView.getWidth();
        //float xScale = screenWidth / rootView.getWidth();
        float yScale = (float) container.getHeight() / rootView.getHeight();
        //float yScale = screenHeigt / rootView.getHeight();
        float scale = Math.min(xScale, yScale);

        // Scale our contents
        if (aspectRatio) xScale = yScale = scale;
        scaleViewAndChildren(rootView, xScale, yScale);
    }
    // Scale the given view, its contents, and all of its children by the given factor.
    private static void scaleViewAndChildren(View root, float scaleX, float scaleY) {
        // Retrieve the view's layout information
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        // Scale the view itself
        if (layoutParams.width != ViewGroup.LayoutParams.FILL_PARENT &&
                layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            layoutParams.width *= scaleX;
        }
        if (layoutParams.height != ViewGroup.LayoutParams.FILL_PARENT &&
                layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            layoutParams.height *= scaleY;
        }

        // If this view has margins, scale those too
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginParams =
                    (ViewGroup.MarginLayoutParams) layoutParams;
            marginParams.leftMargin *= scaleX;
            marginParams.rightMargin *= scaleX;
            marginParams.topMargin *= scaleY;
            marginParams.bottomMargin *= scaleY;
        }

        // Set the layout information back into the view
        root.setLayoutParams(layoutParams);

        // Scale the view's padding
        root.setPadding(
                (int) (root.getPaddingLeft() * scaleX),
                (int) (root.getPaddingTop() * scaleY),
                (int) (root.getPaddingRight() * scaleX),
                (int) (root.getPaddingBottom() * scaleY));

        // If the root view is a TextView, scale the size of its text
        if (root instanceof TextView) {
            TextView textView = (TextView) root;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * scaleY);
        }

        // If the root view is an ImageView, adjust the scale type to FIT_XY
        if (root instanceof ImageView) {

            ImageView imageView = (ImageView) root;

            if (imageView.getScaleType() != ImageView.ScaleType.CENTER_INSIDE)
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        // If the root view is a ViewGroup, scale all of its children recursively
        if (root instanceof ViewGroup) {
            ViewGroup groupView = (ViewGroup) root;
            for (int cnt = 0; cnt < groupView.getChildCount(); ++cnt)
                scaleViewAndChildren(groupView.getChildAt(cnt), scaleX, scaleY);
        }
    }
}
