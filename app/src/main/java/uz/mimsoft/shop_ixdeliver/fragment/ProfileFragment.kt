package uz.mimsoft.shop_ixdeliver.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.view_error.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uz.mimsoft.shop_ixdeliver.ApiFactory
import uz.mimsoft.shop_ixdeliver.PManager
import uz.mimsoft.shop_ixdeliver.R
import uz.shopix.agentapp.SERVER_IMAGES_ADDRESS
import java.io.File

class ProfileFragment   : Fragment() {
    companion object {
        private val fragment = ProfileFragment()
        fun getInstance() = fragment
    }

    private val compositeDisposable = CompositeDisposable()
    private var image: Image? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateData()
    }


    private fun onCreateData() {
        activity?.title = getString(R.string.profile_edit_title)
        ivAvatar.isClickable = false

        etUserName.setText(PManager.getName(), TextView.BufferType.EDITABLE)
        etPhone.setText(PManager.getPhone(), TextView.BufferType.EDITABLE)

        Picasso.get()
                .load(PManager.getImage())
                .placeholder(R.drawable.ic_account)
                .centerCrop()
            .error(R.drawable.ic_alert)
                .fit()
                .into(ivAvatar)

        ivAvatar.setOnClickListener {

            ImagePicker.create(this)
                    .folderMode(true)
                    .single()
                    .theme(R.style.AppTheme_NoActionBar)
                    .start()
        }
        vError.tvRetry.setOnClickListener {
            if (image == null) {
                setData()
            } else {
                setData()
                upDataImage()
            }
        }
        ivAvatar.isClickable = false
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btDone -> {
                if (!etUserName.isEnabled) {
                    etUserName.isEnabled = true
                    etPhone.isEnabled = true
                    ivAvatar.isClickable = true
                    item.setIcon(R.drawable.ic_done)

                } else {
                    setData()
                    item.setIcon(R.drawable.ic_pencil)
                    etUserName.isEnabled = false
                    etPhone.isEnabled = false
                    ivAvatar.isClickable = false
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData() {
        compositeDisposable.add(ApiFactory
                .getApiService()
                .setProfileData(PManager.getKey(), etUserName.text.toString(), etPhone.text.toString())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    showLoading()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code == 0) {
                        PManager.setName(etUserName.text.toString())
                        PManager.setPhone(etPhone.text.toString())

//                        nv!!.getHeaderView(0).findViewById<AppCompatTextView>(R.id.tvName).text = PManager.getName()
//                        nv.getHeaderView(0).findViewById<AppCompatTextView>(R.id.tvPhone).text = PManager.getPhone()
                        Picasso.get()
                            .load(PManager.getImage())
                            .into(nv.getHeaderView(0).findViewById<AppCompatImageView>(R.id.ivNavigationView))

                    } else {
                        AlertDialog.Builder(requireContext())
                                .setTitle(getString(R.string.error))
                                .setMessage(getString(R.string.unknown_error))
                                .show()
                    }
                    showFragment()
                }, {
                    showError()
                }))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            image = ImagePicker.getFirstImageOrNull(data)
            upDataImage()
        }
    }

    private fun upDataImage() {

        val file = File(image!!.path)
        val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData("SurveyImage", file.name, surveyBody)

        compositeDisposable.add(ApiFactory
                .getApiService()
                .upDataDeliverImage(PManager.getKey(), imagePart)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    pbImage.visibility = View.VISIBLE
                    ivAvatar.visibility = View.GONE
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code == 0) {
                        PManager.setImage(SERVER_IMAGES_ADDRESS + it.result)
                        Picasso.get()
                                .load(PManager.getImage())
                                .into(ivAvatar)

//                        MainActivity().onChangeImage()

//                        TODO change image in navigationview
                        Toast.makeText(requireContext(), getString(R.string.photo_loaded_successfully), Toast.LENGTH_LONG).show()

                    } else {
                        AlertDialog.Builder(requireContext())
                                .setTitle(resources.getString(R.string.error))
                                .setMessage(resources.getString(R.string.unknown_error))
                                .show()
                    }
                    ivAvatar.visibility = View.VISIBLE
                    pbImage.visibility = View.GONE
                }, {
                    showError()
                }))

    }

    private fun showLoading() {
        vLoading.visibility = View.VISIBLE
        vError.visibility = View.GONE
    }

    private fun showError() {
        vLoading.visibility = View.GONE
        vError.visibility = View.VISIBLE
    }

    private fun showFragment() {
        vLoading.visibility = View.GONE
        vError.visibility = View.GONE
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}