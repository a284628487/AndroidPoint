# Fragment

- hide()

隐藏当前的Fragment，仅仅是设为不可见，并不会销毁，不会调用`onDestroyView`和`onDestroy`方法。

- show()

显示之前隐藏的Fragment

`show()`方法和`hide()`方法都不会销毁View(`onDestroyView`不会被调用)

- detach()

会将view从UI中移除(调用`onDestroyView`方法)，和`remove()`不同，此时`fragment`的状态依然由`FragmentManager`维护，`fragment`实例并没被`destory(onDestory)`，
再次调用`attach()`方法，将会重新调用`onCreateView`方法来创建view。`detach()`方法和`attach()`方法，都并不会调用`Fragment`的`onDetach`和`onAttach`方法。

- remove()
和`detach()`有一点细微的区别，在不考虑回退栈的情况下，`remove()`会销毁整个`Fragment`实例，而`detach`则只是销毁其视图结构，实例并不会被销毁。

- detach() vs remove()

如果当前`Activity`一直存在，那么在不希望保留用户操作的时候，可以优先使用`detach`。如果调用了`ft.addToBackStack("a")`将当前的事务添加到了回退栈，则`Fragment`实例不会被销毁，但是视图层次依然会被销毁，即会调用`onDestoryView`和`onCreateView`；

### DialogFragment

```
public class MyDialogFragment extends DialogFragment {
   
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.sorting_options, null);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(dialogView);
        dialog.setTitle(R.string.sort_by);
        dialog.show();

        return dialog;
    }
}
```

- 在styles文件中配置Dialog对应的主题:可以配置是否显示Title等

```
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
    <item name="android:windowBackground">@color/window_background</item>
    <item name="android:dialogTheme">@style/TitledDialog</item>
</style>

<style name="TitledDialog" parent="@style/Theme.AppCompat.Light.Dialog">
    <item name="android:windowNoTitle">true</item>
</style>
```

- 显示Dialog

```
MyDialogFragment mdf = new MyDialogFragment();
mdf.show(getFragmentManager(), "test");
```
