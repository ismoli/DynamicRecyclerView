DynamicRecyclerView
===================

Set of light and non-invasive extensions for Android RecyclerView widget. Does not use custom RecyclerView or LayoutManager.
With this extensions you can create RecyclerView with following features:

###Drag and drop reordering.
- Implemented using RecyclerView.OnItemTouchListener
- Support for custom "Drag frame" drawable
- ~350 LOC

#####Usage:
```java
    dragDropTouchListener = new DragDropTouchListener(recyclerView, this) {
        @Override
        protected void onItemSwitch(RecyclerView recyclerView, int from, int to) {
            adapter.swapPositions(from, to);
            adapter.notifyItemChanged(to);
            adapter.notifyItemChanged(from);
 
         @Override
         protected void onItemDrop(RecyclerView recyclerView, int position) {
        }
   };
   }
   
    recyclerView.addOnItemTouchListener(dragDropTouchListener);
```
###Swipe to dismiss items
This is port of Roman Nurik's [SwipeToDismiss for ListView](https://github.com/romannurik/Android-SwipeToDismiss)
- Implemented using RecyclerView.OnItemTouchListener
- Configurable swipe directions: only left, only right, both, none
- ~320 LOC 

#####Usage:
```java
 swipeToDismissTouchListener = new SwipeToDismissTouchListener(recyclerView, new SwipeToDismissTouchListener.DismissCallbacks() {
            @Override
           public SwipeToDismissTouchListener.SwipeDirection canDismiss(int position) {
               return SwipeToDismissTouchListener.SwipeDirection.RIGHT;
           }
            @Override
           public void onDismiss(RecyclerView view, List<SwipeToDismissTouchListener.PendingDismissData> dismissData) {
              for (SwipeToDismissTouchListener.PendingDismissData data : dismissData) {
                  adapter.removeItem(data.position);
                  adapter.notifyItemRemoved(data.position);
              }
           }
   });
  recyclerView.addOnItemTouchListener(swipeToDismissTouchListener);
```

###Select/activate items
- Small RecyclerViewAdapter extension that can keep a state of selected/activated items

#####Usage
Use as normal RecyclerView.Adapter

###ItemTouchListenerAdapter
As RecyclerView does not have standard way to add click listeners to the items, this `RecyclerView.OnItemTouchListener` intercepts touch events and translates them to simple `onItemClick()` and `onItemLongClick()` callbacks.

#####Usage
```java
    recyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView, this));
```

###Sample
Sample app code is included, please see [DemoActivity](app/src/main/java/com/du/android/recyclerview/sample/DemoActivity.java).

For full featured demo of real app see [/du:/ tasks app](https://play.google.com/store/apps/details?id=com.du.android) on Google Play Store

