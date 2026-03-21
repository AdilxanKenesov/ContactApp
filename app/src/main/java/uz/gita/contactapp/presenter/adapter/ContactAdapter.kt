package uz.gita.contactapp.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.contactapp.data.model.contact.response.ContactResponse
import uz.gita.contactapp.databinding.ItemContactBinding


class ContactAdapter: ListAdapter<ContactResponse, ContactAdapter.ContactViewHolder>(diffItem){
    companion object{
        val diffItem = object : DiffUtil.ItemCallback<ContactResponse>(){
            override fun areItemsTheSame(oldItem: ContactResponse, newItem: ContactResponse): Boolean {
                return oldItem.phone == newItem.phone
            }

            override fun areContentsTheSame(oldItem: ContactResponse, newItem: ContactResponse): Boolean {
                return oldItem == newItem
            }

        }
    }

    private var onItemClick: ((ContactResponse)-> Unit)? = null

    fun setOnClickListener(f: (ContactResponse) -> Unit){
        onItemClick = f
    }

    inner class ContactViewHolder(private val bindin: ItemContactBinding): RecyclerView.ViewHolder(bindin.root){

        fun bind(data: ContactResponse){
            bindin.textName.text = data.name
            bindin.textPhone.text = data.phone

            bindin.root.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}