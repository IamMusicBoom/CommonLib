package foundation.widget.tag;

import java.util.List;

/**
 *单选 多选
 */
public interface OnTagSelectListener {
    void onItemSelect(FlowTagLayout parent, int positoin, List<Integer> selectedList);
}
