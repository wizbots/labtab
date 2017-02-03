package org.wizbots.labtab.retrofit;

import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.markabsent.MarkStudentAbsentResponse;
import org.wizbots.labtab.model.program.response.ProgramResponse;
import org.wizbots.labtab.model.promotedemote.PromotionDemotionResponse;
import org.wizbots.labtab.model.student.response.StudentResponse;
import org.wizbots.labtab.model.video.response.CreateProjectResponse;
import org.wizbots.labtab.model.video.response.EditProjectResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface LabTabApiInterface {

    String BASE_URL = "http://45.33.27.64/api/";

    //    Operations related to deal with tokens

    //    1. Creates New Token
    @FormUrlEncoded
    @POST("tokens/")
    Call<CreateTokenResponse> createTokenOrLoginUser(@Field("password") String password, @Field("email") String email);


    //    2. Deletes A Token
    @POST("tokens/{id}")
    Call<Object> deleteToken(@Path("id") String id, @Header("Auth-Token") String authToken);


    //    3. Returns A Token
    @GET("tokens/{id}")
    Call<Object> returnToken(@Path("id") String id, @Header("Auth-Token") String authToken);

    //    4. Returns list of programs
    @GET("programs/")
    Call<ArrayList<ProgramOrLab>> returnPrograms(@Header("Auth-Token") String authToken);

    //    5. Returns list of programs using member_id,from and to
    @GET("programs/")
    Call<ArrayList<ProgramOrLab>> returnPrograms(@Header("Auth-Token") String authToken, @Query("mentor_id") String mentor_id,
                                                 @Query("from") String from, @Query("to") String to);

    //    6. Returns a mentor
    @GET("mentors/")
    Call<Mentor> returnMentor(@Header("Auth-Token") String authToken);

    //    7. Returns a program with a list of students
    @GET("programs/{id}")
    Call<ProgramResponse> returnProgramWithListOfStudents(@Path("id") String id, @Header("Auth-Token") String authToken);


    //    8. Returns a student
    @GET("students/{id}")
    Call<StudentResponse> returnStudentProfileAndStats(@Path("id") String id, @Header("Auth-Token") String authToken);

    //    9. Mark Student Absent
    @POST("students/mark-absent")
    Call<MarkStudentAbsentResponse> markStudentAbsent(@Header("Auth-Token") String authToken, @Query("students") String[] students,
                                                      @Query("date") String date, @Query("program") String program,
                                                      @Query("send_notification") boolean sendNotification);

    //    10. Promote Or Demote Student
    @POST("students/promotion")
    Call<PromotionDemotionResponse> promoteDemoteStudent(@Header("Auth-Token") String authToken, @Query("students") String[] students,
                                                         @Query("promote") boolean promoteDemote);


    //   11. Create A Project With New Video File
    @Multipart
    @POST("projects/")
    Call<CreateProjectResponse> createProject(@Header("Auth-Token") String authToken, @Query("category") String category,
                                              @Query("sku") String sku, @Query("description") String description,
                                              @Query("title") String title, @Query("notes") String notes,
                                              @Part MultipartBody.Part file,
                                              @Query("components") String[] components,
                                              @Query("creators") String[] creators);

    //   12. Edit A Project
    @PUT("projects/{project_id}")
    Call<EditProjectResponse> editProject(@Header("Auth-Token") String authToken, @Path("project_id") String project_id, @Query("category") String category, @Query("description") String description,
                                          @Query("title") String title, @Query("notes") String notes,
                                          @Query("components") String[] components,
                                          @Query("creators") String[] creators);


}
