package com.mmcs.societymaintainance.util;

import com.mmcs.societymaintainance.model.ComplaintRestMeta;
import com.mmcs.societymaintainance.model.DesignationRestMeta;
import com.mmcs.societymaintainance.model.EmployeeRestMeta;
import com.mmcs.societymaintainance.model.LoginResMeta;
import com.mmcs.societymaintainance.model.OwnerRestMeta;
import com.mmcs.societymaintainance.model.ResAttandance;
import com.mmcs.societymaintainance.model.ResponseMeta;
import com.mmcs.societymaintainance.model.SettingsModel;
import com.mmcs.societymaintainance.model.UnitRestMeta;
import com.mmcs.societymaintainance.model.UploadImageResMeta;
import com.mmcs.societymaintainance.model.VisitorRestMeta;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApi {


    @FormUrlEncoded
    @POST("apilogin.php")
    Call<LoginResMeta> login(@Field("username") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("fcm_token") String fcm_token, @Field("ddlBranch") String ddlBranch, @Field("ddlLoginType") String ddlLoginType);

    @FormUrlEncoded
    @POST("visitor_get_api.php")
    Call<VisitorRestMeta> getVisitorList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);

    @FormUrlEncoded
    @POST("get_visitorbyid_api.php")
    Call<VisitorRestMeta> getVisitorById(@Field("id") String id);


    @FormUrlEncoded
    @POST("complain_get_api.php")
    Call<ComplaintRestMeta> getComplaintList(@Field("id") String id, @Field("ddlLoginType") String type, @Field("branch_id") String branch_id, @Field("floor_id") String floor_id, @Field("unit_id") String unit_id);


    @FormUrlEncoded
    @POST("owner_get_api.php")
    Call<OwnerRestMeta> getOwnerList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);


    @FormUrlEncoded
    @POST("employee_get_api.php")
    Call<EmployeeRestMeta> getEmployeeList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);

    @FormUrlEncoded
    @POST("maid_get_api.php")
    Call<EmployeeRestMeta> getMaidList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);


    @FormUrlEncoded
    @POST("driver_get_api.php")
    Call<EmployeeRestMeta> getDriverList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);

    @FormUrlEncoded
    @POST("guard_get_api.php")
    Call<EmployeeRestMeta> getGuardList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);

    @FormUrlEncoded
    @POST("other_get_api.php")
    Call<EmployeeRestMeta> getOthersList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);

    @Multipart
    @POST("add_other_post_api.php")
    Call<LoginResMeta> postOthers(@Part("other_type") RequestBody other_type, @Part("id") RequestBody id, @Part("ddlLoginType") RequestBody ddlLoginType, @Part("branch_id") RequestBody branch_id,
                                  @Part("txtEmpName") RequestBody txtEmpName, @Part("txtEmpEmail") RequestBody txtEmpEmail, @Part("txtEmpContact") RequestBody txtEmpContact, @Part("txtEmpPreAddress") RequestBody txtEmpPreAddress,
                                  @Part("txtEmpPerAddress") RequestBody txtEmpPerAddress, @Part("id_type") RequestBody txtIDType, @Part("txtEmpNID") RequestBody txtEmpNID, @Part("ddlMemberType") RequestBody ddlMemberType, @Part("txtEmpDate") RequestBody txtEmpDate, @Part("txtEndingDate") RequestBody txtEndingDate, @Part("e_password") RequestBody e_password, @Part("e_status") RequestBody e_status, @Part("added_date") RequestBody added_date, @Part("image\"; filename=\"profile.jpg") RequestBody image

    );


    @FormUrlEncoded
    @POST("floorlist_get_api.php")
    Call<ResponseMeta> getFloorList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);

    @FormUrlEncoded
    @POST("unitlist_get_api.php")
    Call<UnitRestMeta> getUnitList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);


    @FormUrlEncoded
    @POST("branch_get_api.php")
    Call<UnitRestMeta> getBranchId(@Field("id") String id);

    @FormUrlEncoded
    @POST("settings.php")
    Call<SettingsModel> getSettings(@Field("id") String id);


    @FormUrlEncoded
    @POST("get_designation_api.php")
    Call<DesignationRestMeta> getDesignationList(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);

   /* @FormUrlEncoded
    @POST("get_designation_api.php")
    Call<DesignationRestMeta> getEmployeeDepartment(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id);

*/
    @Multipart
    @POST("add_visitor_post_api.php")
    Call<LoginResMeta> postVisitor(@Part("id") RequestBody id, @Part("ddlLoginType") RequestBody ddlLoginType, @Part("branch_id") RequestBody branch_id,
                                   @Part("txtName") RequestBody txtName, @Part("txtIssueDate") RequestBody txtIssueDate, @Part("txtMobile") RequestBody txtMobile, @Part("txtAddress") RequestBody txtAddress,
                                   @Part("ddlFloorNo") RequestBody ddlFloorNo, @Part("ddlUnitNo") RequestBody ddlUnitNo, @Part("txtInTime") RequestBody txtInTime, @Part("txtOutTime") RequestBody txtOutTime, @Part("image\"; filename=\"profile.jpg") RequestBody image

    );

    @Multipart
    @POST("add_owner_post_api.php")
    Call<LoginResMeta> postOwner(@Part("id") RequestBody id, @Part("ddlLoginType") RequestBody ddlLoginType, @Part("branch_id") RequestBody branch_id,
                                 @Part("txtOwnerName") RequestBody txtOwnerName, @Part("txtOwnerEmail") RequestBody txtOwnerEmail, @Part("txtOwnerContact") RequestBody txtOwnerContact, @Part("txtOwnerPreAddress") RequestBody txtOwnerPreAddress,
                                 @Part("txtOwnerPerAddress") RequestBody txtOwnerPerAddress, @Part("id_type") RequestBody txtIDType, @Part("txtOwnerNID") RequestBody txtOwnerNID, @Part("o_password") RequestBody o_password, @Part("floor_id") RequestBody floor_id, @Part("unit_id") RequestBody unit_id, @Part("image\"; filename=\"profile.jpg") RequestBody image

    );

    @FormUrlEncoded
    @POST("add_complain_post_api.php")
    Call<LoginResMeta> postComplaint(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id, @Field("dept") String dept, @Field("txtCTitle") String txtCTitle
            , @Field("txtCDescription") String txtCDescription, @Field("txtCDate") String txtCDate, @Field("xmonth") String xmonth, @Field("xyear") String xyear,
                                     @Field("floor_id") String floor_id, @Field("unit_id") String unit_id
    );
    @FormUrlEncoded
    @POST("update_complain_api.php")
    Call<ComplaintRestMeta> postComplaintStatus(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("branch_id") String branch_id, @Field("status") String status, @Field("comment") String comment, @Field("c_id") String c_id

    );


    @Multipart
    @POST("add_employee_post_api.php")
    Call<LoginResMeta> postEmployee(@Part("id") RequestBody id, @Part("ddlLoginType") RequestBody ddlLoginType, @Part("branch_id") RequestBody branch_id,
                                    @Part("txtEmpName") RequestBody txtEmpName, @Part("txtEmpEmail") RequestBody txtEmpEmail,
                                    @Part("txtEmpContact") RequestBody txtEmpContact,
                                    @Part("txtEmpPreAddress") RequestBody txtEmpPreAddress,
                                    @Part("txtEmpPerAddress") RequestBody txtEmpPerAddress, @Part("id_type") RequestBody txtIDType, @Part("txtEmpNID") RequestBody txtEmpNID,
                                    @Part("ddlMemberType") RequestBody ddlMemberType, @Part("txtEmpDate") RequestBody txtEmpDate,
                                    @Part("txtEndingDate") RequestBody txtEndingDate, @Part("e_password") RequestBody e_password,
                                    @Part("e_status") RequestBody e_status, @Part("added_date") RequestBody added_date, @Part("image\"; filename=\"profile.jpg") RequestBody image

    );

    @Multipart
    @POST("add_maid_post_api.php")
    Call<LoginResMeta> postMaid(@Part("id") RequestBody id, @Part("ddlLoginType") RequestBody ddlLoginType, @Part("branch_id") RequestBody branch_id,
                                @Part("txtEmpName") RequestBody txtEmpName, @Part("txtEmpEmail") RequestBody txtEmpEmail, @Part("txtEmpContact") RequestBody txtEmpContact, @Part("txtEmpPreAddress") RequestBody txtEmpPreAddress,
                                @Part("txtEmpPerAddress") RequestBody txtEmpPerAddress, @Part("id_type") RequestBody txtIDType, @Part("txtEmpNID") RequestBody txtEmpNID, @Part("ddlMemberType") RequestBody ddlMemberType, @Part("txtEmpDate") RequestBody txtEmpDate, @Part("txtEndingDate") RequestBody txtEndingDate, @Part("e_password") RequestBody e_password, @Part("e_status") RequestBody e_status, @Part("added_date") RequestBody added_date, @Part("image\"; filename=\"profile.jpg") RequestBody image

    );

    @Multipart
    @POST("add_driver_post_api.php")
    Call<LoginResMeta> postDriver(@Part("id") RequestBody id, @Part("ddlLoginType") RequestBody ddlLoginType, @Part("branch_id") RequestBody branch_id,
                                  @Part("txtEmpName") RequestBody txtEmpName, @Part("txtEmpEmail") RequestBody txtEmpEmail, @Part("txtEmpContact") RequestBody txtEmpContact, @Part("txtEmpPreAddress") RequestBody txtEmpPreAddress,
                                  @Part("txtEmpPerAddress") RequestBody txtEmpPerAddress, @Part("id_type") RequestBody txtIDType, @Part("txtEmpNID") RequestBody txtEmpNID, @Part("ddlMemberType") RequestBody ddlMemberType, @Part("txtEmpDate") RequestBody txtEmpDate, @Part("txtEndingDate") RequestBody txtEndingDate, @Part("e_password") RequestBody e_password, @Part("e_status") RequestBody e_status, @Part("added_date") RequestBody added_date, @Part("image\"; filename=\"profile.jpg") RequestBody image


    );



    @Multipart
    @POST("add_guard_post_api.php")
    Call<LoginResMeta> postGuard(@Part("id") RequestBody id, @Part("ddlLoginType") RequestBody ddlLoginType, @Part("branch_id") RequestBody branch_id,
                                 @Part("txtEmpName") RequestBody txtEmpName, @Part("txtEmpEmail") RequestBody txtEmpEmail, @Part("txtEmpContact") RequestBody txtEmpContact, @Part("txtEmpPreAddress") RequestBody txtEmpPreAddress,
                                 @Part("txtEmpPerAddress") RequestBody txtEmpPerAddress, @Part("id_type") RequestBody txtIDType, @Part("txtEmpNID") RequestBody txtEmpNID, @Part("ddlMemberType") RequestBody ddlMemberType, @Part("txtEmpDate") RequestBody txtEmpDate, @Part("txtEndingDate") RequestBody txtEndingDate, @Part("e_password") RequestBody e_password, @Part("e_status") RequestBody e_status, @Part("added_date") RequestBody added_date, @Part("image\"; filename=\"profile.jpg") RequestBody image

    );


    @Multipart
    @POST("update_profile_api.php")
    Call<UploadImageResMeta> updateUserProfile(@Part("id") RequestBody id, @Part("ddlLoginType") RequestBody ddlLoginType, @Part("branch_id") RequestBody branch_id
            , @Part("image\"; filename=\"profile.jpg") RequestBody image
    );
    @FormUrlEncoded
    @POST("update_visitor_api.php")
    Call<UnitRestMeta> updateVisitor(@Field("vid") String vid, @Field("txtOutTime") String txtOutTime);

    @FormUrlEncoded
    @POST("update_visitor_status_api.php")
    Call<UnitRestMeta> updateVisitorStatus(@Field("id") String id, @Field("status") String status);

    @FormUrlEncoded
    @POST("update_employee_api.php")
    Call<UnitRestMeta> updateEmployee(@Field("eid") String eid, @Field("ending_date") String ending_date);

    @FormUrlEncoded
    @POST("update_other_api.php")
    Call<UnitRestMeta> updateOthers(@Field("eid") String eid, @Field("ending_date") String ending_date, @Field("other_type") String other_type);


    @FormUrlEncoded
    @POST("update_fcm_token.php")
    Call<UnitRestMeta> updateToken(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType, @Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST("logout_api.php")
    Call<UnitRestMeta> logOut(@Field("id") String id, @Field("ddlLoginType") String ddlLoginType);
    @FormUrlEncoded

    @POST("get_complain_byid_api.php")
    Call<ComplaintRestMeta> getComplainById(@Field("id") String id);

    @FormUrlEncoded
    @POST("complain_action_api.php")
    Call<ComplaintRestMeta> complainAction(@Field("c_id") String c_id, @Field("assign_id") String assign_id);


    @FormUrlEncoded
    @POST("attendance_post_api.php")
    Call<ResAttandance> postAttendance(@Field("user_id") String user_id, @Field("location") String location, @Field("date_time") String datetime, @Field("status") String status, @Field("date") String date, @Field("type") String type

    );

    @FormUrlEncoded
    @POST("attendance_get_api.php")
    Call<ResAttandance> getAttandanceStatus(@Field("user_id") String user_id, @Field("type") String type

    );

}
